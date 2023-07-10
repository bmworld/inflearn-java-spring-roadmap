package com.example.servlet.web.servletmvc;


import com.example.servlet.basic.repository.MemberRepository;
import com.example.servlet.domain.member.Member;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "mvcMemberListServlet", urlPatterns = "/servlet-mvc/members")
public class MvcMemberListServlet extends HttpServlet {

  private MemberRepository memberRepository = MemberRepository.getInstance();


  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {


    List<Member> members = memberRepository.findAll();


    // Model 내에 데이터 보관
    req.setAttribute("members", members);

    // View 호출
    String viewPath = "/WEB-INF/views/members.jsp";
    RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
    dispatcher.forward(req, res);

  }

}
