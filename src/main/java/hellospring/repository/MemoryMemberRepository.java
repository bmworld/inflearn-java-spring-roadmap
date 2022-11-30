package hellospring.repository;

import hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMemberRepository implements MemberRepository {

  private static final Map<Long, Member> store = new HashMap<>();
  private static long sequence = 0; // sequence: 0, 1 ,2...등을 생성해주는 key 값

  @Override
  public Member save(Member member) {
    member.setId(++sequence); // id는 system이 정해주는 걸로 세팅.
    store.put(member.getId(), member);
    return member;
  }

  @Override
  public Optional<Member> findById(Long id) {
    return Optional.ofNullable(store.get(id)); // null값 처리를 해줘야한다. // Optional로 감싸주면, null값일 경우에 client에서 뭔가를 할 수 있다.
  }

  @Override
  public Optional<Member> findByName(String name) {
    return store.values().stream().filter(member -> member.getName().equals(name)).findAny();
  }

  @Override
  public List<Member> findAll() {
    return new ArrayList<>(store.values());
  }


  public void clearStore(){
    store.clear();
  }


}


// ! 이 코드가 제대로 작동하는지 검증하기 위한 방법? TEST CASE.