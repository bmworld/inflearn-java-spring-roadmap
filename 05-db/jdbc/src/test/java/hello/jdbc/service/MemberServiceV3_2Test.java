package hello.jdbc.service;

import hello.jdbc.Repository.MemberRepositoryV3;
import hello.jdbc.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

/**
 * <h1>Transaction Ver.</h1>
 */
class MemberServiceV3_2Test {

  public static final String MEMBER_A = "memberA";
  public static final String MEMBER_B = "memberB";
  public static final String MEMBER_Exception = "ex";

  private MemberRepositoryV3 memberRepository;
  private MemberServiceV3_2 memberService;


  @BeforeEach
  void before() throws SQLException {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);

    memberRepository = new MemberRepositoryV3(dataSource);

    // =================================================================
    // ! DataSourceTransactionManager 또한, Datasource가 있어야 Connection Source를 만들 수 있다
    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
    // =================================================================

    memberService = new MemberServiceV3_2(transactionManager, memberRepository);

    memberRepository.deleteAll();
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
