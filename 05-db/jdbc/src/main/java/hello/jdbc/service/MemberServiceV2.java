package hello.jdbc.service;

import hello.jdbc.Repository.MemberRepositoryV2;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * <h1>Transaction with Param</h1>
 * <pre>
 *   - 트랜잭션 실행 조건
 *     1. Connection 유지
 *     2. Manual Commit mode
 *
 *     FLOW: 서비스 계층에서 connection 생성 => Transaction 및 Commit => Connection 종료
 * </pre>
 * <h2>- Connection Pool 안정성을 위해, Connection을 Pool에 반환하기 전에, Commit mode `Manual -> Auto` 변경시키시라.</h2>
 */
@RequiredArgsConstructor
@Slf4j
public class MemberServiceV2 {

  private final DataSource dataSource;
  private final MemberRepositoryV2 memberRepository;

  public void accountTransfer(String fromId, String toId, int money) throws SQLException {
    Connection con = dataSource.getConnection();


    try{
      // ******************** START TRANSACTION ********************
      con.setAutoCommit(false); // => set autocommit false;

      // ******************** BUSINESS LOGIC ********************
      bizLogic(con, fromId, toId, money);

      // ******************** COMMIT ********************
      con.commit(); // 성공 시, Commit 실행


    } catch(Exception e) {
      // ******************** ROLLBACK ********************
      con.rollback(); // 예외발생(실패) 시, Rollback
      System.out.println("******************** ROLLBACK ********************");
      throw new IllegalStateException(e);
    } finally {
      release(con);
    }


  }

  private void bizLogic(Connection con, String fromId, String toId, int money) throws SQLException {
    Member fromMember = memberRepository.findById(con, fromId);
    Member toMember = memberRepository.findById(con, toId);

    int fromMoney = fromMember.getMoney() - money;
    int toMoney = toMember.getMoney() + money;


    // 돈 뺌
    memberRepository.update(con, fromId, fromMoney);
    // 검증 성공 시, 다음으로 넘어감
    validation(toMember);
    // 돈 추가
    memberRepository.update(con, toId, toMoney);
  }

  private void release(Connection con) {
    if (con != null) {
      try{
        // ******************** INITIALIZE TRANSACTION ********************
        // 자동 커밋으로 돌려놓는다. => Connection 풀 고려한 선택이다.
        //
        con.setAutoCommit(true);
        // ******************** CLOSE CONNECTION ********************
        con.close();
      } catch(Exception e) {
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
