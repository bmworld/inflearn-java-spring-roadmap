package hellospring.controller;

import hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

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
  @Autowired
  private final MemberService memberService;
  public MemberController (MemberService memberService){

    this.memberService = memberService;
  }

}