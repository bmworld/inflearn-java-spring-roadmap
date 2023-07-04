package com.example.servlet.basic.response;

import com.example.servlet.basic.HelloData;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

  private ObjectMapper objectMapper = new ObjectMapper(); // springboot integrated Library as default


  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {


    res.setContentType("application/json"); // json
    res.setCharacterEncoding("UTF-8");


    HelloData data = new HelloData();
    data.setAge(11);
    data.setName("bm");

    String result = objectMapper.writeValueAsString(data);

    PrintWriter writer = res.getWriter();
    writer.write(result);


  }


}
