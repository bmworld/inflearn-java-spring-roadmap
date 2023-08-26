package hello.jdbc.Repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * <h1>Transaction Manager</h1>
 * <pre>
 *   - DataSourceUtils: 트랙잭션 동기화 매니저가 관리하는 커넥션
 *
 *
 *   - DataSourceUtils.getConnection()
 *     -> 커넥션 존재: 해당 커넥션을 반환
 *     -> 커넥션 비존재: 신규 생성 후 반환.
 *
 *
 *   - DataSourceUtils.releaseConnection()
 *     -> 트랜잭션을 사용하기 위해 동기화된 커넥션은 닫지 않고 유지함.
 *     -> 관리 대상 커넥션 없을 경우, 해당 커넥션을 닫음
 * </pre>
 *
 */
@Slf4j
public class MemberRepositoryV3 {
  public MemberRepositoryV3(DataSource dataSource) {
    this.dataSource = dataSource;
  }


  private final DataSource dataSource;

  /**
   * getConnection 사용 X / Param 으로 넘어온 connection 사용해야함.
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
      close(con, pstmt, rs);
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
      close(con, pstmt, null);
    }
  }


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


  public void delete(String memberId) throws SQLException {
    String sql = "DELETE FROM MEMBER WHERE member_id=?";

    Connection con = null;
    PreparedStatement pstmt = null;


    try {

      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.setString(1, memberId);
      int resultSize = pstmt.executeUpdate(); // Query 실행으로 영향받은 `row` count 반환.

      log.info("delete > resultSize = {}", resultSize);

    } catch (SQLException e) {
      log.error("---- delete > db error", e);
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








  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================

  private void close (Connection con, Statement stmt, ResultSet rs) {
    JdbcUtils.closeResultSet(rs);
    JdbcUtils.closeStatement(stmt);
    // 주의 ! 트랜잭션 동기화 사용하기 위해서는 DataSourceUtils를 반드시 사용하시라.
    DataSourceUtils.releaseConnection(con, dataSource);
  }


  private Connection getConnection() throws SQLException {
    // 주의 ! 트랜잭션 동기화 사용하기 위해서는 DataSourceUtils를 반드시 사용하시라.

    Connection con = DataSourceUtils.getConnection(dataSource);
    log.info("MemberRepositoryV1 > getConnection={}, class={}", con, con.getClass());
    return con;

  }
}
