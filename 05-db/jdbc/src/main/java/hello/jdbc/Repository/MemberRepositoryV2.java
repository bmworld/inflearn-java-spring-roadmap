package hello.jdbc.Repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * <h1>JDBC -  Transaction Version</h1>
 * <h2>Connection을 각 메서드에 주입받고, Connection을 닫는 것도, 호출하는 곳에서 처리하도록 한다.</h2>
 */
@Slf4j
public class MemberRepositoryV2 {
  public MemberRepositoryV2(DataSource dataSource) {
    this.dataSource = dataSource;
  }


  private final DataSource dataSource;

  /**
   * getConnection 사용 X / Param 으로 넘어온 connection 사용해야함.
   */
  public Member findById(Connection con, String memberId) throws SQLException {
    String sql = "SELECT * FROM Member WHERE member_id = ?";

    PreparedStatement pstmt = null;
    ResultSet rs = null;

    try {

      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, memberId);

      rs = pstmt.executeQuery();


      System.out.println("--- rs = " + rs); // e.g) rs0: columns: 2 rows: 1 pos: -1


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
      close(pstmt, rs);
    }
  }



  public void update(Connection con, String memberId, int money) throws SQLException {
    String sql = "UPDATE member SET money=? WHERE member_id=?";

    PreparedStatement pstmt = null;


    try {
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
      close(pstmt, null);
    }
  }


  public void delete(Connection con, String memberId) throws SQLException {
    String sql = "DELETE FROM MEMBER WHERE member_id=?";

    PreparedStatement pstmt = null;


    try {
      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, memberId);
      int resultSize = pstmt.executeUpdate(); // Query 실행으로 영향받은 `row` count 반환.

      log.info("delete > resultSize = {}", resultSize);

    } catch (SQLException e) {
      log.error("---- delete > db error", e);
      e.getStackTrace();
      throw e;
    } finally {
      close(pstmt, null);
    }
  }


  public void deleteAll(Connection con) throws SQLException {

    String sql = "DELETE FROM Member";

    PreparedStatement pstmt = null;

    try {
      pstmt = con.prepareStatement(sql);
      pstmt.executeUpdate();

    } catch (Exception e) {
      log.error("---- deleteAll > db error", e);
      throw  e;
    } finally {
      close(pstmt, null);
    }

  }





  public Member save(Member member) throws SQLException {

    String sql = "INSERT INTO MEMBER(member_id, money) VALUES (?,?)";

    Connection con = null;
    PreparedStatement pstmt = null;


    try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, member.getMemberId());
      pstmt.setInt(2, member.getMoney());

      int resultRowCount = pstmt.executeUpdate();
      return member;
    } catch (SQLException e) {
      log.error("db error", e);
      e.getStackTrace();
      throw e;
    } finally {
      close(pstmt, null);
    }
  }




  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================

  private void close (Statement stmt, ResultSet rs) {
    JdbcUtils.closeResultSet(rs);
    JdbcUtils.closeStatement(stmt);
//    JdbcUtils.closeConnection(con); // ! Transaction 적용을 위해, Connection을 Repository에서 닫지 않는다.
  }


  private Connection getConnection() throws SQLException {
    Connection con = dataSource.getConnection();
    log.info("MemberRepositoryV1 > getConnection={}, class={}", con, con.getClass());
    return con;

  }
}
