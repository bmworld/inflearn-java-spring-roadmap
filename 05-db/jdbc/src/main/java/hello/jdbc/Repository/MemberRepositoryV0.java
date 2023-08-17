package hello.jdbc.Repository;

import hello.jdbc.connection.DBConnectionUtils;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.NoSuchElementException;

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

  public void update(String memberId, int money) throws SQLException {
    String sql = "UPDATE member SET money=? WHERE member_id=?";

    Connection con = null;
    PreparedStatement pstmt = null;


    try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setInt(1, money);
      pstmt.setString(2, memberId);
      int resultSize = pstmt.executeUpdate(); // Query 실행으로 영향받은 `row` count 반환.

      log.info("update > resultSize = {}", resultSize);

    } catch (SQLException e) {
      log.error("---- update > db error", e);
      e.getStackTrace();
      throw e;
    } finally {
      close(con, pstmt, null);
    }
  }


  public void deleteAll() throws SQLException {

    String sql = "DELETE FROM Member";

    Connection con = null;
    PreparedStatement pstmt = null;

    try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.executeUpdate();

    } catch (Exception e) {
      log.error("---- deleteAll > db error", e);
      throw  e;
    } finally {
      close(con, pstmt, null);
    }

  }


  /**
   * <h1>ResultSet</h1>
   * <pre>
   *   - select query 날렸을 때 보이는 그 테이블 구조랑 똑같음
   *   - ResultSet 내부 `cursor`를 이동하여, 다음 데이터를 조회할 수 있다.
   *   - `rs.next()`: cursor가 다음 row로 이동,
   *     -> 최초의 cursor는 DATA를 가르키고 있지 않음.
   *     -> 따라서, 데티어 조회를 위해 최초 rs.next() 최초 한번 호출 필요.
   *
   *   - rs.getString("..."): 현재 cursor가 위치한 row의 column 데이터를  `String Type`으로 반환.
   *   - rs.getInt("..."): 현재 cursor가 위치한 row의 column 데이터를 `int Type`으로 반환.
   *
   *   - rs.next() -> 데이터가 없는 경우, `false` 반환.
   *
   * </pre>
   */
  public Member findById(String memberId) throws SQLException {
    String sql = "SELECT * FROM Member WHERE member_id = ?";

    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, memberId);

      rs = pstmt.executeQuery();


      System.out.println("--- rs = " + rs);


      // rs.next() 를 한번은 실행해줘야, 실제 데이터에 있는 것부터 실행이 된다.
      if (rs.next()) {
        Member member = new Member();
        member.setMemberId(rs.getString("member_id"));
        member.setMoney(rs.getInt("money"));
        return member;
      } else {
        // resultSet에서 `커서`가 있는데... 거기에서 데이터가 없는 경우.

        throw new NoSuchElementException("Member not found memberId=" + memberId);
      }

    } catch (Exception e) {
      log.error("---- findById > db error", e);
      throw  e;
    } finally {
      close(con, pstmt, rs);
    }
  }




  public Member save(Member member) throws SQLException {

    String sql = "INSERT INTO MEMBER(member_id, money) VALUES (?,?)";

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




  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================

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
