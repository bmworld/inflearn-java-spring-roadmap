package com.example.servlet.web.springmvc.old;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


// 옛날 방식 컨트롤러 => "Spring bean 이름"으로 Handler를 찾는다.
@Component("/springMvc/old-controller") // spring bean 이름을 url 패턴으로 만들어주면, 이 컨트롤러가 호출이된다!!
public class OldController implements Controller {
  @Override
  public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
    System.out.println("OldController.handleRequest");
    return new ModelAndView("new-form");
  }
}
