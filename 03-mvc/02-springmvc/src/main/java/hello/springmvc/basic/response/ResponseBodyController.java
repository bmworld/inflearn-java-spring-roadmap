package hello.springmvc.basic.response;

import hello.springmvc.basic.request.HelloData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
//@Controller
//@ResponseBody // 클래스 레벨에 붙일 경우, 전체 Method 일괄 적용
@RestController // => @Controller + @ResponseBody
public class ResponseBodyController {

  @RequestMapping("/response-body-string-v1")
  public void responseBodyStringV1(
    HttpServletResponse res
  ) throws IOException {

    res.getWriter().write("OK by HttpServletResponse res.getWriter().write");
  }


  @RequestMapping("/response-body-string-v2")
  public ResponseEntity responseBodyStringV2(
  ) {
    return new ResponseEntity<>("OK by new ResponseEntity()", HttpStatus.OK);
  }


  @RequestMapping("/response-body-string-v3")
  public String responseBodyStringV3(
  ) {
    return "OK by @ResponseBody";
  }

  @GetMapping("/response-body-json-v1")
  public ResponseEntity<HelloData> responseBodyJsonV1() {
    HelloData data = new HelloData();
    data.setUsername("bm");
    data.setAge(33);
    return new ResponseEntity<>(data, HttpStatus.OK);
  }


  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/response-body-json-v2")
  public HelloData responseBodyJsonV2() {
    HelloData data = new HelloData();
    data.setUsername("bm");
    data.setAge(33);
    return data;
  }


}
