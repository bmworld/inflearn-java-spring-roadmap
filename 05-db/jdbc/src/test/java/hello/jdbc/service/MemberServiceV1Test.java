package hello.jdbc.service;

import hello.jdbc.Repository.MemberRepositoryV1;
import hello.jdbc.connection.ConnectionConst;
import hello.jdbc.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 기본 동작, 트랜잭션이 없어서 문제 발생함.
 */
class MemberServiceV1Test {

  public static final String MEMBER_A = "memberA";
  public static final String MEMBER_B = "memberB";
  public static final String MEMBER_Exception = "ex";

  private MemberRepositoryV1 memberRepository;
  private MemberServiceV1 memberService;


  @BeforeEach
  void before() throws SQLException {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    memberRepository = new MemberRepositoryV1(dataSource);
    memberService = new MemberServiceV1(memberRepository);

    memberRepository.deleteAll();
  }


  @DisplayName("정상 이체")
  @Test
  void accountTransfer () throws SQLException {
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


  @DisplayName("정상 이체 - 실패 without 트랜잭션 => Rollback 없음")
  @Test
  void accountTransfer_failedByException () throws SQLException {
    // Given
    int beforeMemberMoney_A = 10000;
    int beforeMoney_B = 10000;
    Member memberA = new Member(MEMBER_A, beforeMemberMoney_A);
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
    // 트랜젝션이 없으므로, 예외발생 => MEMBER A 돈만 까짐
    Member foundMemberA = memberRepository.findById(memberId_A);
    Member foundMemberB = memberRepository.findById(memberId_B);

    int afterMoney_A = beforeMemberMoney_A - diffMoney;
    int afterMoney_B = beforeMoney_B + diffMoney;

    Assertions.assertThat(foundMemberA.getMoney()).isEqualTo(afterMoney_A);
    Assertions.assertThat(foundMemberB.getMoney()).isEqualTo(beforeMoney_B);

  }
}
