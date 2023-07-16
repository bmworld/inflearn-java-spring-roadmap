package com.example.servlet.web.springmvc.v1;

import com.example.servlet.basic.repository.MemberRepository;
import com.example.servlet.domain.member.Member;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SpringMemberSaveControllerV1 {
  private MemberRepository memberRepository = MemberRepository.getInstance();

  @RequestMapping("/springmvc/v1/members/save")
  public ModelAndView process(HttpServletRequest req, HttpServletResponse res) {
    String name = req.getParameter("username");
    int age = Integer.parseInt(req.getParameter("age"));
    Member member = new Member(name, age);
    memberRepository.save(member);


    ModelAndView mv = new ModelAndView("save-result");
    mv.addObject("member", member);
    return mv;

  }
}
