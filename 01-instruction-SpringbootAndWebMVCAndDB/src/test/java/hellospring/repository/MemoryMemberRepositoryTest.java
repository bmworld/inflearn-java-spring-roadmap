package hellospring.repository;
import hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import static org.assertj.core.api.Assertions.*;

// ! 중요 :  TEST케이스는 '순서보장'이 안.된.다.
// ! ***** TEST 케이스는 순서와 상관없이, 독립적으로 진행되도록 만들어야 한다. *****
// 따라서, 테스트가 끝난 케이스 ==>  DATA CLEAR !


class MemoryMemberRepositoryTest {
  MemoryMemberRepository repository = new MemoryMemberRepository();
  
  @AfterEach
  public void afterEach(){
    repository.clearStore();
    // TEST가 끝날 때마다, 저장소를 다 지운다 (MemoryMemberRepository에 new HashMap<>().clear() 기능)
  }

  @Test
  public void save () {
    Member member = new Member();
    member.setName("bmworld");

    repository.save(member); // 현재 저장한 녀석
    Member result  = repository.findById(member.getId()).get(); // (memory) DB에서 꺼낸거


    // 내가 저정한 것과, DB에서 꺼낸 것이 동일하면, 통과
    // Assertions.assertEquals(result, member); ("Junit" Ver.)
    assertThat(member).isEqualTo(result);   // ("assertj" Ver.)
//    assertThat(null).isEqualTo(result);   // ("assertj" Ver.)

    // ***** Add on-demand static import를 사용하면 ? *****
    // import static org.assertj.core.api.Assertions.*; <-- 이렇게 추가를 시키고.
    // Assertions.assertThat(member).isEqualTo(result); ==> assertThat(member).isEqualTo(result);
    // 위와 같이 코드를 줄일 수 있다.

  }

  @Test
  public void findByName () {
    Member member1 = new Member();
    member1.setName("spring1");
    repository.save(member1);


    Member member2 = new Member();
    member2.setName("spring2");
    repository.save(member2);

    Member result = repository.findByName("spring1").get();


    assertThat(result).isEqualTo(member1); // SUCCESS CASE

    Member result2 = repository.findByName("spring2").get();
//    assertThat(result2).isEqualTo(member1); // ERROR CASE

  }

  @Test
  public void findAll() {
    Member member1 = new Member();
    member1.setName("spring1");
    repository.save(member1);

    Member member2 = new Member();
    member2.setName("spring2");
    repository.save(member2);

    List<Member> result = repository.findAll();
    assertThat(result.size()).isEqualTo(2); // Success Case
//    assertThat(result.size()).isEqualTo(3); // Error case

  }



}
