package hellospring.controller;

import hellospring.domain.Member;
import hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class MemberController {
  /*
   여기까지하면, 최초에 Spring에 의해서 Spring Container가 생성된다.
   @Controller Anotation이 존재하면, 해당 객체를 미리 생성해서, Spring에 넣는다 & 관리한다..
  */

  //  private final MemberService memberService = new MemberService();
  // ** new 생성자로 인스턴스륾 만들어 쓴다면 => 다른 컨트롤레어 해당 객체를 가져다 쓴다는문제가 발생한다.!
  // ** 1 개만 생성할 필요가 있을 때는,
  // Constructor를 사용하여, Spring Container에 등록해서 사용
  // @Autowired Anotation 사용 =>  Spring이 memberService객체를 Controller 생성자 호출하는 시점에 Controller와 연결시켜준다.


  //////////////////////////////////////////////////////
  // # D.I (Dependency Injection : 생성자를 통한 의존성 주입) Ver.
  // => 생성자를 통해서, MemberService가 MemberController에 주입이 됨.
  // 최초에 생성자를 통해서 딱 1회만 생성되고, 누군가가 호출할 수도 없기 때문에,
  // 가장 적당한 방법이다.
  private final MemberService memberService;

  @Autowired

  public MemberController(MemberService memberService) {
    this.memberService = memberService;

  }
  //////////////////////////////////////////////////////


  //////////////////////////////////////////////////////
  // # F.I ( Field Injection ) Ver.
  // => 필드주입은, 좋은 방법은 아니다.
  // why? 뭔가를 바꿀 수 있는 방법이 없다.
  //@Autowired private MemberService memberService;
  //////////////////////////////////////////////////////


  //////////////////////////////////////////////////////
  // # S.I ( Setter Injection ) Ver.
  // => 누군가가 이 MemberController를 호출했을 떄, setter가 public으로서 열려있어야함..
  // => 사실 Repository는 처음 세팅외에는 중간에 setter를 쓸 이유가 없기 때문에, SI는 적당한 방법이 아님.
  // => 심지어, 누가 중간에 Repository를 setter를 통해서 잘못 바꿔버렸다.-_- 그럼 정말 큰일난다..
//  private MemberService memberService;
//  public void setMemberService (MemberService memberService){
//    this.memberService = memberService;
//  }
  //////////////////////////////////////////////////////

  @GetMapping("/members/new")
  public String createForm() {
    return "/members/createMemberForm"; // 별다른거 안하고, createMemberForm.html을 반환하는 역할만 한다.
  }

  @PostMapping("/members/new")
  public String create(MemberForm form) {
    Member member = new Member();
    String name = form.getName();
    member.setName(name);

    System.out.println("new member = " + member.getName());

    memberService.join(member);
    return "redirect:/"; // 회원가입이 끝나면, 루트 페이지로 보낸다.
  }

  @GetMapping("/members")
  public String list(Model model){

    List<Member> members = memberService.findMembers();
    System.out.println("all members = " + members);
    model.addAttribute("members", members);
    return "/members/memberList";
  }
}