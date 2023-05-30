package hellospring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import util.Testing;

@Controller
public class HelloController {

  @GetMapping("hellospring")
  public String hello (Model model) {
    model.addAttribute("data", "thymeleaf를 사용하여, controller에서 추가한 attributeValue~~~!!!!!");
    return "hellospring"; // resources/static/templates 내부에 존재하는 html파일명과 매칭시킴.
    // 컨트롤러에서 return 값으로 문자를 반환하면 `viewResolver`가 화면을 찾아서 처리한다.

  }


  @GetMapping("hello-mvc")
  public String helloMvc(@RequestParam(value = "name", required = false) String name, Model model){
    model.addAttribute("name", name);
    return "hello-template";
  }


  @GetMapping("hello-spring")
  @ResponseBody
  public String helloSpring (@RequestParam("name") String name) {
    return "hellospring" + name;
  }

  @GetMapping("hello-api")
  @ResponseBody
  public Hello helloApi(@RequestParam("name") String name) {
    Hello hello = new Hello();
    hello.setName(name);

    return hello;
  }

  public static class Hello {
    private String name;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }



  @GetMapping("hello-api-testing")
  @ResponseBody
  public Testing helloApiTesting(@RequestParam("name") String name) {
    Testing t = new Testing();
    t.setName(name);

    return t;
  }


}


