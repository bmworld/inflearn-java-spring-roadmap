package com.example.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

    System.out.println("[전체 파라미터 조회]");
    Enumeration<String> parameterNames = req.getParameterNames();
    parameterNames.asIterator().forEachRemaining(paramName -> System.out.println("paramName = " + paramName));

    System.out.println("----------------------------");

    System.out.println();
    System.out.println("[단일 파라미터 조회]");
    String name = req.getParameter("name");
    String age = req.getParameter("age");
    System.out.println("age = " + age);
    System.out.println("name = " + name);
    System.out.println();

    System.out.println("----------------------------");

    System.out.println("[이름이 동일한 복수 파라미터 조회]");
    String[] names = req.getParameterValues("name");
    for (String n : names) {
      System.out.println("복수 파라미터 > name = " + n);

    }

    System.out.println("----------------------------");

    res.setStatus(HttpServletResponse.SC_OK);
    res.setCharacterEncoding("utf-8");
    res.setContentType("text/html;charset=UTF-8");
    PrintWriter writer = res.getWriter();
    writer.flush();
    writer.println(
      "<html>" +
        "<h1>" + "RequestParamPage" + "</h1>" +
        "<p> name = " + name + "</p>" +
        "<p> age = " + age + "</p>" +
        "</html>"
    );


  }
}
