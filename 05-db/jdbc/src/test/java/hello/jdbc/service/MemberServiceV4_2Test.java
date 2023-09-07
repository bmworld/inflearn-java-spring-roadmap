package hello.jdbc.service;

import hello.jdbc.Repository.MemberRepositoryV4_2;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.BadSqlGrammarException;

import javax.sql.DataSource;

/**
 * <h1>MemberRepository Interface 의존 Ver.</h1>
 * <pre>
 *   - Exception 누수 문제 해결
 *   - SQL Exception 제거 (Checked Exception -> Unchecked Exception)
 * </pre>
 */
@Slf4j
@SpringBootTest
class MemberServiceV4_2Test {

  public static final String MEMBER_A = "memberA";
  public static final String MEMBER_B = "memberB";
  public static final String MEMBER_Exception = "ex";

  @Autowired
  private MemberRepositoryV4_2 memberRepository; // 얘들 Bean으로 반드시 등록하시라

  @Autowired
  private MemberServiceV4 memberService; // // 얘들 Bean으로 반드시 등록하시라


  @TestConfiguration
  static class TestConfig {
    private final DataSource dataSource; //  Spring Container에 등록된 DataSource를 가져온다.

    TestConfig(DataSource dataSource) {
      this.dataSource = dataSource;
    }

    @Bean
    MemberRepositoryV4_2 MemberRepository() {
      return new MemberRepositoryV4_2(dataSource);
    }

    @Bean
    MemberServiceV4 memberServiceV4() {
      return new MemberServiceV4(MemberRepository());
    }
  }

  @BeforeEach
  void before() {
    memberRepository.deleteAll();
  }


  @Test
  @DisplayName("Proxy 적용여부 확인")
  void AopCheck() {


    // =================================
    log.info("--- memberService class ={}", memberService.getClass()); // hello.jdbc.service.MemberServiceV3_3$$EnhancerBySpringCGLIB$$9a429050
    // $$EnhancerBySpringCGLIB$$9a429050 이게 붙어있으면, 해당 이 Service Logic을 상속받아서, 해당 코드를 @Override(카피 뜬다)한다.


    // =================================
    log.info("--- memberRepository class ={}", memberRepository.getClass()); // hello.jdbc.Repository.MemberRepositoryV3


    // ================================= 검증
    Assertions.assertThat(AopUtils.isAopProxy(memberService)).isTrue();
    Assertions.assertThat(AopUtils.isAopProxy(memberRepository)).isFalse();
  }


  @DisplayName("SQL Exception Translator 확인")
  @Test
  void sqlExceptionTranslator () {
    // Given
    String invalidSQL = "INSERT INTO adlskfjasldkfj(member_id, money) VALUES (?,?)";

    Assertions.assertThatThrownBy(() -> memberRepository.saveByInvalidSQL(generateMembers(MEMBER_A, 10000), invalidSQL)).isInstanceOf(BadSqlGrammarException.class);
    Assertions.assertThatThrownBy(() -> memberRepository.saveByInvalidSQL(generateMembers(MEMBER_B, 20000), invalidSQL)).isInstanceOf(BadSqlGrammarException.class);

    // sample org.springframework.jdbc.BadSqlGrammarException: save; bad SQL grammar [ INSERT INTO adlskfjasldkfj(member_id, money) VALUES (?,?)]; nested exception is org.h2.jdbc.JdbcSQLSyntaxErrorException: Table "ADLSKFJASLDKFJ" not found; SQL statement:

  }

  @DisplayName("정상 이체")
  @Test
  void accountTransfer() {
    // Given
    int beforeMoney_A = 10000;
    int beforeMoney_B = 10000;
    Member memberA = generateMembers(MEMBER_A, beforeMoney_A);
    Member memberB = generateMembers(MEMBER_Exception, beforeMoney_B);


    memberRepository.save(memberA);
    memberRepository.save(memberB);


    String memberId_A = memberA.getMemberId();
    String memberId_B = memberB.getMemberId();
    int diffMoney = 2000;
    memberService.accountTransfer(memberId_A, memberId_B, diffMoney);

    // Then
    Member foundMemberA = memberRepository.findById(memberId_A);
    Member foundMemberB = memberRepository.findById(memberId_B);

    int afterMoney_A = beforeMoney_A - diffMoney;
    int afterMoney_B = beforeMoney_B + diffMoney;

    Assertions.assertThat(foundMemberA.getMoney()).isEqualTo(afterMoney_A);
    Assertions.assertThat(foundMemberB.getMoney()).isEqualTo(afterMoney_B);

  }


  @DisplayName("정상 이체 - 실패 with 트랜잭션 => Rollback 실행됨")
  @Test
  void accountTransfer_failedByException() {
    // Given

    int beforeMoney_A = 10000;
    int beforeMoney_B = 10000;
    Member memberA = generateMembers(MEMBER_A, beforeMoney_A);
    Member memberB = generateMembers(MEMBER_Exception, beforeMoney_B);

    memberRepository.save(memberA);
    memberRepository.save(memberB);


    // When
    String memberId_A = memberA.getMemberId();
    String memberId_B = memberB.getMemberId();
    int diffMoney = 2000;
    Assertions.assertThatThrownBy(() -> memberService.accountTransfer(memberId_A, memberId_B, diffMoney))
      .isInstanceOf(IllegalStateException.class)
    ;

    // Then
    // 트랜젝션있으므로, 모두 다, 초기값으로 바뀜
    Member foundMemberA = memberRepository.findById(memberId_A);
    Member foundMemberB = memberRepository.findById(memberId_B);


    Assertions.assertThat(foundMemberA.getMoney()).isEqualTo(beforeMoney_A);
    Assertions.assertThat(foundMemberB.getMoney()).isEqualTo(beforeMoney_B);

  }

  private Member generateMembers(String memberA, int beforeMoney) {
    return new Member(memberA, beforeMoney);
  }
}
