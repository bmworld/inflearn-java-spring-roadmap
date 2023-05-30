package hellospring.repository;

import hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
  Member save(Member member);
  Optional<Member> findById(Long id); // Optional: Java8에 추가된 기능
  Optional<Member> findByName(String name); // Optional: Java8에 추가된 기능 // null값이 반환되었을 경우, optional로 감싸서 반환받음.
  List<Member> findAll();
}
