package hello.jdbc.service;

import hello.jdbc.Repository.MemberRepositoryV3;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.SQLException;


@RequiredArgsConstructor
@Slf4j
public class MemberServiceV3_1 {

//  private final DataSource dataSource; // <- transactionManager 사용시에는 불필요


  /**
   * MemberServiceV3_1 초기화 시 주의사항
   * MemberServiceV3_1초기화 시, transactionManager는  `Datasource`가 있어야만 DB와 연결할 수 있다.
   */
  private final PlatformTransactionManager transactionManager;

  private final MemberRepositoryV3 memberRepository;

  public void accountTransfer(String fromId, String toId, int money) {
    // ******************** START TRANSACTION ********************
    TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

    try {


      // ******************** BUSINESS LOGIC ********************
      bizLogic(fromId, toId, money);

      // ******************** COMMIT ********************
      transactionManager.commit(status);


    } catch (Exception e) {
      // ******************** ROLLBACK ********************
      transactionManager.rollback(status); // 예외발생(실패) 시, Rollback
      System.out.println("******************** ROLLBACK ********************");
      throw new IllegalStateException(e);
    }

    // [NOTICE]
    // release: TransactionManager가 알아서 관리한다. 내가 신경쓸 피룡 없음


  }

  private void bizLogic(String fromId, String toId, int money) throws SQLException {
    Member fromMember = memberRepository.findById(fromId);
    Member toMember = memberRepository.findById(toId);

    int fromMoney = fromMember.getMoney() - money;
    int toMoney = toMember.getMoney() + money;


    // 돈 뺌
    memberRepository.update(fromId, fromMoney);
    // 검증 성공 시, 다음으로 넘어감
    validation(toMember);
    // 돈 추가
    memberRepository.update(toId, toMoney);
  }

  private void release(Connection con) {
    if (con != null) {
      try {
        // ******************** INITIALIZE TRANSACTION ********************
        // 자동 커밋으로 돌려놓는다. => Connection 풀 고려한 선택이다.
        //
        con.setAutoCommit(true);
        // ******************** CLOSE CONNECTION ********************
        con.close();
      } catch (Exception e) {
        e.getStackTrace();
        log.info("error message={}", e.getMessage(), e);
      }

    }
  }

  private static void validation(Member toMember) {
    if (toMember.getMemberId().equals("ex")) {
      throw new IllegalStateException("계좌 이체 중 예외 발생");
    }
  }
}
