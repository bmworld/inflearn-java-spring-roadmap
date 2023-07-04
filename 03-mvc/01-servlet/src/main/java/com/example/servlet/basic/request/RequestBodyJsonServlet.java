package com.example.servlet.basic.request;

import com.example.servlet.basic.HelloData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


@WebServlet(name = "requestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {

  private ObjectMapper objectMapper = new ObjectMapper(); // springboot integrated Library as default
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    ServletInputStream inputStream = req.getInputStream();// Http Message Body 를 Byte code로 얻을 수 있음.
    String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    System.out.println("messageBody as JSON = " + messageBody);


    HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
    System.out.println("helloData.getName() = " + helloData.getName());
    System.out.println("helloData.getAge() = " + helloData.getAge());
    res.getWriter().write("ok!");
  }
}
