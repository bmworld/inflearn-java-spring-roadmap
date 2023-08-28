package hello.jdbc.service;

import hello.jdbc.Repository.MemberRepositoryV3;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * <h1>Transaction Template</h1>
 * <pre>
 *   - transaction 사용 시 반복되는 try catch 절을 깔끔하게 줄일 수 있다.
 *
 *   - txTemplate.executeWithoutResult
 *     => 해당 코드 내애ㅔ서 Transaction 시작
 *
 *     1. Business Logic 정상 수행 시, commit 됨
 *     2. Uncheck(Runtime) Exception 발생 시, Rollback
 *        => 그 외에는 commit
 * </pre>
 */
@Slf4j
public class MemberServiceV3_2 {


  // 강사님 => 코드 유연성을 위해, TransationManager 주입받은 후, Template 생성방식 사용 (TransactionTemplate 을 주입받을 경우 유연성 떨어짐 )
  //  private final PlatformTransactionManager transactionManager;
  private final TransactionTemplate txTemplate;

  private final MemberRepositoryV3 memberRepository;


  public MemberServiceV3_2(PlatformTransactionManager transactionManager, MemberRepositoryV3 memberRepository) {
    this.txTemplate = new TransactionTemplate(transactionManager);
    this.memberRepository = memberRepository;
  }
  public void accountTransfer(String fromId, String toId, int money) {
    txTemplate.executeWithoutResult((status) -> {
      try {
        bizLogic(fromId, toId, money);
      } catch (SQLException e) {
        throw new IllegalStateException(e);
      }
    });
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

//  private void release(Connection con) {
//    if (con != null) {
//      try {
//        // ******************** INITIALIZE TRANSACTION ********************
//        // 자동 커밋으로 돌려놓는다. => Connection 풀 고려한 선택이다.
//        //
//        con.setAutoCommit(true);
//        // ******************** CLOSE CONNECTION ********************
//        con.close();
//      } catch (Exception e) {
//        e.getStackTrace();
//        log.info("error message={}", e.getMessage(), e);
//      }
//
//    }
//  }

  private static void validation(Member toMember) {
    if (toMember.getMemberId().equals("ex")) {
      throw new IllegalStateException("계좌 이체 중 예외 발생");
    }
  }
}
