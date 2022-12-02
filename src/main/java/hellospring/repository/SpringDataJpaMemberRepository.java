package hellospring.repository;

import hellospring.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpringDataJpaMemberRepository extends JpaRepository<Member,Long>, MemberRepository {

  // ! Spring Data JPA는 다음 코드를 아래의 인터페이스 이름을 통해 구현한다.
  // => select m from Member m where m.name = ?
  // => ex. findByNameAndId(String name, Long id); => 이름과 네임 모두 충족하는 값을 찾아냄.
  @Override
  Optional<Member> findByName(String name);


}
