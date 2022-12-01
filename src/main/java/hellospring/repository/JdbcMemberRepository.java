package hellospring.repository;

import hellospring.domain.Member;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class JdbcMemberRepository implements MemberRepository{

  // 0-1 단계 bundle.gradle에 h2database설치
  // 0-2단계 /src/main/java/application.properties 값 추가
  // # 1단계, DB랑 붙이려면, datasource가 필요하다.
  // # 2단걔. 현재 JdbcMemberRepository에 interface를 implements하고, 그에 맞는 구현체를 기술한다.


  // # STEP .1 DB DATA SOURCE가 필요하다.
  private final DataSource dataSource; // Spring에게 주입받은 DB를 통해서 진행.


  public JdbcMemberRepository(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public Member save(Member member) {

    String sql = "insert into member(name) values(?)";
    Connection conn = null; // DB와의 Connection을 가져온다.
    PreparedStatement pstmt = null; // Sql을 넣는다.
    ResultSet rs = null; // 결과들을 받기 위한 변수.


    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      pstmt.setString(1, member.getName()); //  parmeter Index는 String sql의 vaues(?) 내부의 물음표와 매칭된다.
      pstmt.executeUpdate(); // DB에 쿼리를 날린다.
      rs = pstmt.getGeneratedKeys(); // DB 생성 시, 몇 번째 인덱스인지 Statement.RETURN_GENERATED_KEYS를 통하여 반환받을값을 통해 generateKey를 하는 것이다.

      if(rs.next()){ // ResultSet의 값이 존재할 경우 next()메서드가 작동한다.
        member.setId(rs.getLong(1));
      } else{
        throw new SQLException("ID조회 실패");
      }

      return member;

    } catch (Exception e) {
      throw new IllegalStateException(e);

    } finally {
      close(conn, pstmt, rs);
      // !!! 주의; DB의 자원을 사용하면 나면, 연.결.을.끊.어.야.한.다 // 그렇지 않을 경우, Database Connection이 계속 쌓여서, 대 장 애 가 발 생 할 수 이 따.

    }

  }



  @Override
  public Optional<Member> findById(Long id) {

    String sql = "select * from member where id = ?";

    Connection conn = null; // DB와의 Connection을 가져온다.
    PreparedStatement pstmt = null; // Sql을 넣는다.
    ResultSet rs = null; // 결과들을 받기 위한 변수.

    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(sql);
      pstmt.setLong(1, id); //  parmeter Index는 String sql의 vaues(?) 내부의 물음표와 매칭된다.

      rs = pstmt.executeQuery();

      if(rs.next()){ // ResultSet의 값이 존재할 경우 next()메서드가 작동한다.
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("name"));
        return Optional.of(member);

      } else{

        return Optional.empty();

      }


    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      close(conn, pstmt, rs);
      // !!! 주의; DB의 자원을 사용하면 나면, 연.결.을.끊.어.야.한.다 // 그렇지 않을 경우, DB Connection이 계속 쌓여서, 대 장 애 가 발 생 할 수 이 따.
    }
  }

  @Override
  public Optional<Member> findByName(String name) {

    // ! findByName ==>  강의 중에 김영한 강사님이 복붙하셔서 빠진 부분.........이다.
    // ! findByName ==>  강의 중에 김영한 강사님이 복붙하셔서 빠진 부분.........이다.
    // ! findByName ==>  강의 중에 김영한 강사님이 복붙하셔서 빠진 부분.........이다.

    String sql = "select * from member where name = ?";

    Connection conn = null; // DB와의 Connection을 가져온다.
    PreparedStatement pstmt = null; // Sql을 넣는다.
    ResultSet rs = null; // 결과들을 받기 위한 변수.

    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(sql);
      pstmt.setString(1, name);

      rs = pstmt.executeQuery();

      if(rs.next()){ // ResultSet의 값이 존재할 경우 next()메서드가 작동한다.
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("name"));
        return Optional.of(member);

      } else{

        return Optional.empty();

      }


    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      close(conn, pstmt, rs);
      // !!! 주의; DB의 자원을 사용하면 나면, 연.결.을.끊.어.야.한.다 // 그렇지 않을 경우, DB Connection이 계속 쌓여서, 대 장 애 가 발 생 할 수 이 따.
    }
  }

  @Override
  public List<Member> findAll() {
    String sql = "select * from member";

    Connection conn = null; // DB와의 Connection을 가져온다.
    PreparedStatement pstmt = null; // Sql을 넣는다.
    ResultSet rs = null; // 결과들을 받기 위한 변수.

    try {
      conn = getConnection();
      pstmt = conn.prepareStatement(sql);

      rs = pstmt.executeQuery();
      List<Member> members = new ArrayList<>();

      while (rs.next()){ // ResultSet의 값이 존재할 경우 next()메서드가 작동한다.
        Member member = new Member();
        member.setId(rs.getLong("id"));
        member.setName(rs.getString("name"));
        members.add(member);
      }

      return members;


    } catch (Exception e) {
      throw new IllegalStateException(e);
    } finally {
      close(conn, pstmt, rs);
      // !!! 주의; DB의 자원을 사용하면 나면, 연.결.을.끊.어.야.한.다 // 그렇지 않을 경우, DB Connection이 계속 쌓여서, 대 장 애 가 발 생 할 수 이 따.
    }
  }

  private Connection getConnection () {
    return DataSourceUtils.getConnection(dataSource);
  }

  private void close (Connection conn, PreparedStatement pstmt, ResultSet rs){
    try {
      if (conn != null) {
        conn.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    try {
      if (pstmt != null) {
        pstmt.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }


    try {

      if (rs != null) {
        rs.close();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void close (Connection conn) throws SQLException {
    DataSourceUtils.releaseConnection(conn, dataSource);
  }


}
