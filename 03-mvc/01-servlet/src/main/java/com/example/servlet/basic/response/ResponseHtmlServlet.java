package com.example.servlet.basic.response;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHtmlServlet", urlPatterns = "/response-html")
public class ResponseHtmlServlet extends HttpServlet {

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {


    res.setContentType("text/html"); // html
    res.setCharacterEncoding("UTF-8");

    PrintWriter writer = res.getWriter();
    writer.println("<html>");
    writer.println("<body>");
    writer.println("<h1>안녕하세요?</h1>");
    writer.println("</body>");
    writer.println("</html>");


  }


}
