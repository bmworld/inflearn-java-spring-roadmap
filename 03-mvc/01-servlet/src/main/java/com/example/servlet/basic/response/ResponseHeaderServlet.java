package com.example.servlet.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {


    System.out.println("--------- RESPONSE - START --------");
    System.out.println("[status-line]");
    res.setStatus(HttpServletResponse.SC_OK);
    System.out.println("[response-header]");

    res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // Delete Cache
    res.setHeader("Pragma", "no-cache"); // Delete Cache - 과거에 저장된 것까지 모두.
    res.setHeader("custom-header-name", "--------------------HELLO!!!");


    System.out.println("[Header 편의 메서드]");
    content(res);
    cookie(res);
    redirect(res);

    System.out.println("[message body]");
    PrintWriter writer = res.getWriter();
    writer.write("<h1>안녕하세요?</h1>");
    System.out.println("----------------------------");


  }

  private void content(HttpServletResponse response) {

//        response.setHeader("Content-Type", "text/plain;charset=utf-8");
    response.setContentType("text/plain");
    response.setCharacterEncoding("utf-8");
//        response.setContentLength(2); //(생략시 자동 생성)
  }

  private void cookie(HttpServletResponse response) {
    //Set-Cookie: myCookie=good; Max-Age=600;
    //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");
    Cookie cookie = new Cookie("myCookie", "good");
    cookie.setMaxAge(600); //600초
    response.addCookie(cookie);
  }

  private void redirect(HttpServletResponse response) throws IOException {

    System.out.println(" Redirect 처리 !! ");

    // 방법1
//        response.setStatus(HttpServletResponse.SC_FOUND); // Status Code 302
//        response.setHeader("Location", "/basic/hello-form.html"); // Location: /basic/hello-form.html

    // 방법2
    response.sendRedirect("/basic/hello-form.html");
  }

}
