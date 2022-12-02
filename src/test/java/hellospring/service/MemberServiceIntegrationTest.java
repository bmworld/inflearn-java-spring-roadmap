package hellospring.service;

import hellospring.domain.Member;
import hellospring.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

// 테스트코드는 빌드에 포함되지 않기 때문에, 한글을 써도 무방하다.

/* < 테스트코드의 기본 골자 >
1. given
  : 조건(또는 데이터).
2. when
  : 검증할 대상
3. then
  : 기대하는 결과
* */

////////////////////////////////////////////////////////////
/*
 ! @Transactional
 ! DB에 transaction 후 테스트가 끝난 뒤, Rollback ===> 따라서, 실제 DB 에 값이 Commit되지 않도록 한다.
 ! DB는 transaction 후, Commit까지 거쳐야, 실제 데이터가 저장되는 방식으로 작동한다
 */
////////////////////////////////////////////////////////////
@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

  @Autowired
  MemberService memberService;
  @Autowired
  MemberRepository memberRepository;


  @Test
//  @Commit // ! @Commit 어노테이션=> DB에 실제 Commit (레알 저장됨)
  void signUp() {
    // given
    Member member = new Member();
    member.setName("SpringDataJPA_USER_2"); // - 보틍 TEST 전용 DB를 구축한다.

    // when
    Long saveId = memberService.join(member);

    // then
    Member foundMember = memberService.findOne(saveId).get();
    assertThat(member.getName()).isEqualTo((foundMember.getName()));
  }




  @Test
  public void duplicatedMemeberException (){
    // given
    Member member1 = new Member();
    member1.setName("spring");

    Member member2 = new Member();
    member2.setName("spring");

    // when
    memberService.join(member1);



//    assertThrows(NullPointerException.class, () -> memberService.join(member2)); //  Fail Case
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));// success Case
    System.out.println(e);
    assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
    // then
  }

}