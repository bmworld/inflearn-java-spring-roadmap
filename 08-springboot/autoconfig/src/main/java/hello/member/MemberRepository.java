package hello.member;

import java.util.List;
import java.util.Objects;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

  public final JdbcTemplate template;

  public MemberRepository(JdbcTemplate template) {
    this.template = template;
  }

  public void initTable() {
    template.execute("create table member(member_id varchar(255) primary key, name varchar(255))");
  }

  public void save(Member member) {
    if (Objects.isNull(member)) {
      throw new IllegalArgumentException("Member cannot be null");
    }

    template.update(
        "insert into member(member_id, name) values(?, ?)", member.getMemberId(), member.getName());
  }

  public Member find(String memberId) {
    final List<Member> members =
        template.query(
            "select member_id, name from member where member_id = ?",
            new BeanPropertyRowMapper<>(Member.class),
            memberId);
    if (members.isEmpty()) {
      return null;
    } else if (members.size() == 1) { // list contains exactly 1 element
      return members.get(0);
    } else {
      // memberId is supposed to be unique, so throw an exception indicating that this is not the
      // case
      throw new IncorrectResultSizeDataAccessException(1, members.size());
    }
  }

  public Member findAll(String memberId) {
    return template.queryForObject(
        "select member_id, name from member", BeanPropertyRowMapper.newInstance(Member.class));
  }
}
