package hello.jdbc.Repository;

import hello.jdbc.domain.Member;

/**
 * <h1>[GOOD CASE] 특정 Exception 종속성 끊기 위해, interface 사용 ( + Exception 제거)</h1>
 */
public interface MemberRepository {

  Member save(Member member);
  Member findById(String id);

  void update(String id, int money);
  void delete(String id);

  void deleteAll();
}
