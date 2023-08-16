package hello.jdbc.Repository;

import hello.jdbc.domain.Member;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class MemberRepositoryV0Test {

  private MemberRepositoryV0 repository = new MemberRepositoryV0();

  @Test
  void save() throws SQLException {

    Member member = new Member("memberV0", 10000);
    repository.save(member);

  }
}
