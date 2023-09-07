package hello.jdbc.Repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;

/**
 * <h1>JDBC template 사용 </h1>
 * <pre>
 *   Connection 동기화 등의 모든 처리를 대신해줌
 * </pre>
 */
@Slf4j
public class MemberRepositoryV5 implements MemberRepository {

  private final JdbcTemplate jdbcTemplate;
  public MemberRepositoryV5(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  public Member findById(String memberId) {
    String sql = "SELECT * FROM Member WHERE member_id = ?";
    return jdbcTemplate.queryForObject(sql, findByIdMemberRowMapper(), memberId);
  }

  @Override
  public Member save(Member member) {
    String sql = "INSERT INTO MEMBER(member_id, money) VALUES (?,?)";
    int update = jdbcTemplate.update(sql, member.getMemberId(), member.getMoney());
    System.out.println("jdbcTemplate > update 된 row 수= " + update);
    return member;

  }



  @Override
  public void update(String memberId, int money) {
    String sql = "UPDATE member SET money=? WHERE member_id=?";
    jdbcTemplate.update(sql, money, memberId);
  }


  @Override
  public void delete(String memberId) {
    String sql = "DELETE FROM MEMBER WHERE member_id=?";
    jdbcTemplate.update(sql, memberId);
  }


  @Override
  public void deleteAll() {
    String sql = "DELETE FROM Member";
    jdbcTemplate.update(sql);
  }

  private RowMapper<Member> findByIdMemberRowMapper() {
    return (rs, rowNum)->{
      Member member = new Member();
      member.setMemberId(rs.getString("member_id"));
      member.setMoney(rs.getInt("money"));
      return member;
    };
  }
}
