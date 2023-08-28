package hello.jdbc.service;

import hello.jdbc.Repository.MemberRepositoryV3;
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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

/**
 * <h1>@Transactional AOP Ver.</h1>
 * <pre>
 *   - 주의. AOP는 Spring Container 등록된 Bean이어야 정상작동.
 *   -> `@SpringBootTest` 사용해야함.
 * </pre>
 */


@Slf4j
@SpringBootTest
class MemberServiceV3_3Test {

  public static final String MEMBER_A = "memberA";
  public static final String MEMBER_B = "memberB";
  public static final String MEMBER_Exception = "ex";

  @Autowired
  private MemberRepositoryV3 memberRepository; // 얘들 Bean으로 반드시 등록하시라

  @Autowired
  private MemberServiceV3_3 memberService; // // 얘들 Bean으로 반드시 등록하시라



  @TestConfiguration
  static class TestConfig {
    @Bean
    DataSource dataSource() {
      return new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    }

    @Bean
    PlatformTransactionManager transactionManager() {
      return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    MemberRepositoryV3 memberRepositoryV3() {
      return new MemberRepositoryV3(dataSource());
    }

    @Bean
    MemberServiceV3_3 memberServiceV3_3() {
      return new MemberServiceV3_3(memberRepositoryV3());
    }
  }
  @BeforeEach
  void before() throws SQLException {
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
  @DisplayName("정상 이체")
  @Test
  void accountTransfer() throws SQLException {
    // Given
    int beforeMoney_A = 10000;
    int beforeMoney_B = 10000;
    Member memberA = new Member(MEMBER_A, beforeMoney_A);
    Member memberB = new Member(MEMBER_B, beforeMoney_B);

    memberRepository.save(memberA);
    memberRepository.save(memberB);
    // When

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
  void accountTransfer_failedByException() throws SQLException {
    // Given

    int beforeMoney_A = 10000;
    int beforeMoney_B = 10000;
    Member memberA = new Member(MEMBER_A, beforeMoney_A);
    Member memberB = new Member(MEMBER_Exception, beforeMoney_B);

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
}
