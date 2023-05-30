package hellospring.service;

import hellospring.domain.Member;
import hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
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
class MemberServiceTest {

  MemberService memberService;
  MemoryMemberRepository memberRepository;
  // 새로운 객체로 가져왔을 경우, memoryRepository와 다른 객체이므로 값이달라질 수 있다.


  // 테스트 실행 '전'에 동작하는 함수
  @BeforeEach
  public void beforeEach() {
    // ! DI (Dependency Injection)
    memberRepository = new MemoryMemberRepository(); // 각 테스트 실행 전, 메모리리포지토리를 신규 생성한다.
    memberService = new MemberService(memberRepository); // repository를 주입해줘서, 매번 테스트마다 독립된 repository가 보장되게 만든다.
  }

  @AfterEach // 클래스가 동작한 '이후' 실행되는 함수
  public void afterEach(){
    // 테스트가 "독립적으로 실행될 수 있도록", clear();
    memberRepository.clearStore();
    System.out.println("테스트 함수?가 실행될 때마다, @AfterEach 어노테이션이 붙은 이 afterEach()함수가 실행된다.");
    // TEST가 끝날 때마다, 저장소를 다 지운다 (MemoryMemberRepository에 new HashMap<>().clear() 기능)
  }

  @Test
  void 회원가입() {
    // given
    Member member = new Member();
    member.setName("hello");

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



    // try catch VER.
//    try {
//      memberService.join(member2);
//      fail("중복회원이 추가되었으므로 예외가 발생해야합니다.");
//    } catch (IllegalStateException e){
//      assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
//    }

//    assertThrows(NullPointerException.class, () -> memberService.join(member2)); //  Fail Case
    IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));// success Case
    System.out.println(e);
    assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");


    // then
  }




  @Test
  void findMembers() {
  }

  @Test
  void findOne() {
  }
}