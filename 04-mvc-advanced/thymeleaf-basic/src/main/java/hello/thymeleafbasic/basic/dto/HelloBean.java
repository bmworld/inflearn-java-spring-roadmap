package hello.thymeleafbasic.basic.dto;

import org.springframework.stereotype.Component;

@Component("helloBean")
public class HelloBean {

  public static String hello(String data) {
    return "Hello" + data;
  }
}
