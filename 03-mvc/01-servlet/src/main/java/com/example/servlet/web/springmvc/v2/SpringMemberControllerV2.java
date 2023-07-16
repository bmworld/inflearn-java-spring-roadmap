package com.example.servlet.web.springmvc.v2;

import com.example.servlet.basic.repository.MemberRepository;
import com.example.servlet.domain.member.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/springmvc/v2/members")
public class SpringMemberControllerV2 {
  private MemberRepository memberRepository = MemberRepository.getInstance();


  @RequestMapping // 더할게 없으면, 클래스단위 내용 그대로 받아진다.
  public ModelAndView members() {

    List<Member> members = memberRepository.findAll();

    ModelAndView mv = new ModelAndView("members");
    mv.addObject("members", members);
    return mv;

  }

  @RequestMapping("/new-form")
  public ModelAndView newForm() {
    return new ModelAndView("new-form");
  }


  @RequestMapping("/save")
  public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
    String name = req.getParameter("username");
    int age = Integer.parseInt(req.getParameter("age"));
    Member member = new Member(name, age);
    memberRepository.save(member);


    ModelAndView mv = new ModelAndView("save-result");
    mv.addObject("member", member);
    return mv;

  }


}
