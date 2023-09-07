package hello.jdbc.Repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * <h1>[GOOD CASE] Exception 누수 문제 해결 Ver.</h1>
 * <pre>
 *   - 변경사항: Checked Exception -> Runtime Exception
 *   - MemberRepository interface 사용
 *   - Throws SQL Exception 제거
 * </pre>
 */
@Slf4j
public class MemberRepositoryV4_2 implements MemberRepository {

  private final DataSource dataSource;

  private final SQLExceptionTranslator exTranslator;

  public MemberRepositoryV4_2(DataSource dataSource) {
    this.dataSource = dataSource;
    exTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);// DataSource 넣어주면, 해당 DB 정보에 맞는 Excpetion을 찾아준다.
  }

  /**
   * getConnection 사용 X / Param 으로 넘어온 connection 사용해야함.
   */
  public Member findById(String memberId) {
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

    } catch (SQLException e) {
      throw exTranslator.translate("findById", sql, e);
    } finally {
      close(con, pstmt, rs);
    }
  }

  @Override
  public Member save(Member member) {

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
      throw exTranslator.translate("save", sql, e);
    } finally {
      close(con, pstmt, null);
    }
  }

  public Member saveByInvalidSQL(Member member, String invalidSQL) {

    Connection con = null;
    PreparedStatement pstmt = null;


    try {
      con = getConnection();
      pstmt = con.prepareStatement(invalidSQL);
      pstmt.setString(1, member.getMemberId());
      pstmt.setInt(2, member.getMoney());

      int resultRowCount = pstmt.executeUpdate();
      return member;
    } catch (SQLException e) {
      throw exTranslator.translate("save", invalidSQL, e);
    } finally {
      close(con, pstmt, null);
    }
  }



  @Override
  public void update(String memberId, int money) {
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
      throw exTranslator.translate("update", sql, e);
    } finally {
      close(con, pstmt, null);
    }
  }


  @Override
  public void delete(String memberId) {
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
      throw exTranslator.translate("delete", sql, e);
    } finally {
      close(con, pstmt, null);
    }
  }


  @Override
  public void deleteAll() {

    String sql = "DELETE FROM Member";

    Connection con = null;
    PreparedStatement pstmt = null;

    try {
      con = getConnection();
      pstmt = con.prepareStatement(sql);
      pstmt.executeUpdate();

    } catch (SQLException e) {
      throw exTranslator.translate("deleteAll", sql, e);
    } finally {
      close(con, pstmt, null);
    }

  }


  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================

  private void close(Connection con, Statement stmt, ResultSet rs) {
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
