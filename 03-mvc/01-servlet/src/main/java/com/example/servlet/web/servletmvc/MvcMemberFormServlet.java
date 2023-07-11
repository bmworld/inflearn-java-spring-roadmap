package com.example.servlet.web.servletmvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    // ! [/WEB-INF] 경로 아래 자원은, 외부 URL로 호출 불가
    // => Controller -> servlet 을 거쳐서 가져올 수 있음.

    // Redirect VS Forward
    // Redirect: 실제 클라에 응답이 나갔다가, 클라가 Redir 경로로 다시 요청 => 따라서 클라측에서 인지 가능
    // Forward: 서버 내부에서 일어나는 호출이므로, 클라가 전혀 알 수 없음.
    String viewPath = "/WEB-INF/views/new-form.jsp";
    RequestDispatcher dispatcher = req.getRequestDispatcher(viewPath);
    dispatcher.forward(req, res);
  }
}
