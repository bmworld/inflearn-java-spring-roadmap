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

@WebServlet(name = "mvcMemberSaveServlet", urlPatterns = "/servlet-mvc/members/save-result")
public class MvcMemberSaveServlet extends HttpServlet {
  private MemberRepository memberRepository = MemberRepository.getInstance();

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    Member member = null;
    try {
      String username = req.getParameter("username");
      int age = Integer.parseInt(req.getParameter("age"));
      member = new Member(username, age);
      memberRepository.save(member);

    } catch (Exception e) {
      e.getStackTrace();
    }



    // Model 내에 데이터 보관
    req.setAttribute("member", member);

    // View 호출
    String viewPath = "/WEB-INF/views/save-result.jsp";
    RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
    dispatcher.forward(req, res);

  }
}
