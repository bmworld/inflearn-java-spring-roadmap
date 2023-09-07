package hello.jdbc.Repository;

import hello.jdbc.domain.Member;

import java.sql.SQLException;

/**
 * <h1>특정 Exception 종속성 제거를 위한 interface 사용</h1>
 * <h1>But Exception 추가한다면, 이와 같이 종속성이 생긴다.</h1>
 */
public interface MemberRepositoryEx {

  Member save(Member member) throws SQLException;
  Member findById(String id) throws SQLException;

  void update(String id, int money) throws SQLException;
  void delete(String id) throws SQLException;

}
