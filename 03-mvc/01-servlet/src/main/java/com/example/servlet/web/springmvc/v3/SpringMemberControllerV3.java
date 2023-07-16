package com.example.servlet.web.springmvc.v3;

import com.example.servlet.basic.repository.MemberRepository;
import com.example.servlet.domain.member.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * * Annotation 기반 HandlerAdapter 가 발동될 경우, String 을 반환하면 알아서 String 에 맞는 ModalAndView를 Return 해준다.
 */
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

  private MemberRepository memberRepository = MemberRepository.getInstance();


  @RequestMapping(method = RequestMethod.GET) // 더할게 없으면, 클래스단위 내용 그대로 받아진다.
  public String members(Model model) {
    List<Member> members = memberRepository.findAll();
    model.addAttribute("members", members);
    return "members";
  }

  @GetMapping(value = "/new-form")
  public String newForm() {
    return "new-form";
  }


  @PostMapping("/save")
  public String save(
    @RequestParam("username") String username,
    @RequestParam("age") int age,
    Model model
  ) {

    Member member = new Member(username, age);
    memberRepository.save(member);

    model.addAttribute("member", member);

    return "save-result";

  }
}
