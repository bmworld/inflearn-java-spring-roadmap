package hello.jdbc.Repository;

import hello.jdbc.connection.DBConnectionUtils;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * <h1>JDBC - DriverManager 사용</h1>
 *<br/
 *
 * <h3>참고) SQL Injection 공격 예방</h3>
 * <pre>
 *   `PreparedStatement` 를 통한 Parameter Binding을 사용해야, Injection을 막을 수 있다.
 *   WHY ? Binding된 부분에 `Query`가 들어오더라도, DATA로 취급하기 때문
 *
 *
 * </pre>
 */
@Slf4j
public class MemberRepositoryV0 {
  public Member save(Member member) throws SQLException {

    String sql = "insert into MEMBER(member_id, money) values (?,?)";

    Connection con = null;
    // PreparedStatement: Parameter Binding 가능한 sql
    // Statement: sql 그대로 사용
    PreparedStatement pstmt = null;


    try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, member.getMemberId());
      pstmt.setInt(2, member.getMoney());

      /**
       * @executeUpdate()
       * 1. 수행결과로 Int 타입의 값을 반환
       * 2. SELECT 구문을 제외한 다른 구문을 수행할 때 사용되는 메서드
       */
      int resultRowCount = pstmt.executeUpdate();
      return member;
    } catch (SQLException e) {
      log.error("db error", e);
      e.getStackTrace();
      throw e;
    } finally {
      // [ 자원반납 ]
      // 리소스는 항상. 꼭. 반납할 것.
      // 자원 반납은 항상 finally 에서 실행할 것.
      // WHY ? try 절내에서 Exception 터지면, 실행되지 않음.
      close(con, pstmt, null);
    }
  }

  private void close (Connection con, Statement stmt, ResultSet rs) {
    if (rs != null) {
      try {
        rs.close(); // Exception 터질 경우, Catch로 잡기 때문에, 아래에 코드 실행에 영향을 끼치지 않음
      } catch (Exception e) {
        log.info("Close ResultSet failed", e);
      }
    }



    if (stmt != null) {
      try {
        stmt.close(); // Exception 터질 경우, Catch로 잡기 때문에, 아래에 코드 실행에 영향을 끼치지 않음
      } catch (Exception e) {
        log.info("Close Statement failed", e);
      }
    }


    if (con != null) {
      try {
        con.close();
      } catch (Exception e) {
        log.info("Close Connection failed", e);
      }
    }


  }

  private Connection getConnection() {
    return DBConnectionUtils.getConnection();
  }
}
