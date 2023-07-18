package hello.springmvc.basic.request;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {
  private String getParamValues(String username, Integer age) {
    return "----- Param values => username= " + username + "/ age = " + age;
  }


  @RequestMapping("/request-param-v1")
  public void requestParamV1(
    HttpServletRequest req,
    HttpServletResponse res
  ) throws IOException {

    String username = req.getParameter("username");
    int age = Integer.parseInt(req.getParameter("age"));

    log.info("username={}, age={}", username, age);
    res.getWriter().write(getParamValues(username, age));
  }


  @RequestMapping("/request-param-v2")
  @ResponseBody // => @RestController와 동일한 효과
  public String requestParamV2(
    @RequestParam("username") String username,
    @RequestParam("age") int age
  ) {
    log.info("username={}, age={}", username, age);

    // 따라서, 문자열 ("OK!!") return 하더라도, view template 조회하지않고, JSON 형식으로 응답.
    return "requestParamV2" + getParamValues(username, age);
  }


  /**
   * 변수명 생략한 경우, 파라미터 이름과 동일해야함.
   */
  @RequestMapping("/request-param-v3")
  @ResponseBody // => @RestController와 동일한 효과
  public String requestParamV3(
    @RequestParam String username,
    @RequestParam int age
  ) {
    log.info("/ GetParamValues => username={}, age={}", username, age);

    // 따라서, 문자열 ("OK!!") return 하더라도, view template 조회하지않고, JSON 형식으로 응답.
    return "requestParamV3" + getParamValues(username, age);
  }



  /**
   * * @RequestParam ===> String integer 등의 단순타입일 경우, @RequestParam 생략가능
   */
  @RequestMapping("/request-param-v4")
  @ResponseBody // => @RestController와 동일한 효과
  public String requestParamV4(
    String username,
    int age
  ) {
    log.info("username={}, age={}", username, age);

    return "requestParamV4" + getParamValues(username, age);
  }


  /**
   * RequestParam Required true일 경우, param에 없으면 에러 발생시킴
   * @param username => 필수 X
   * @param age => 필수
   */

  @RequestMapping("/request-param-required")
  @ResponseBody // => @RestController와 동일한 효과
  public String requestParamRequired(
    @RequestParam(required = false) String username,
    @RequestParam(required = true) Integer age
  ) {
    // ** Integer => null 처리 가능
    // ** int => null 처리 불가
    log.info("username={}, age={}", username, age);

    return "requestParamRequired" + getParamValues(username, age);
  }


  @RequestMapping("/request-param-default")
  @ResponseBody // => @RestController와 동일한 효과
  public String requestParamDefault(
    @RequestParam(required = false, defaultValue = "defaultName") String username,
    @RequestParam(required = true, defaultValue = "10") Integer age
  ) {
    // ** Integer => null 처리 가능
    // ** int => null 처리 불가
    log.info("username={}, age={}", username, age);

    return "requestParamDefault" + getParamValues(username, age);
  }


  @RequestMapping("/request-param-map")
  @ResponseBody // => @RestController와 동일한 효과
  public String requestParamMap(
    @RequestParam Map<String, Object> paramMap
  ) {
    // ** Integer => null 처리 가능
    // ** int => null 처리 불가
    String username = (String) paramMap.get(("username"));
    Integer age = Integer.valueOf((String) paramMap.get(("age")));
    log.info("username={}, age={}", username, age);

    return "requestParamMap" + getParamValues(username, age);
  }


  /**
   * * @ModelAttribute : 다음을 실행한다.
   *   1. HelloData 객체 생성
   *   2. 요청 파라미터 이름으로 "HelloData' 객체 프로퍼티를 찾음
   *   3. 해당 프로퍼티의 Setter 호출 후, 파라미터 값을 Binding
   *   ex. 파라미터이름이 `username` => `setUsername()` 호출 => 조회 시, getUsername 가능  */
  @RequestMapping("/model-attribute-v1")
  @ResponseBody // => @RestController와 동일한 효과
  public String modelAttributeV1(
//    @RequestParam String username,
//    @RequestParam Integer age
    @ModelAttribute HelloData data
  ) {

//    HelloData data = new HelloData();
//    data.setUsername(username);
//    data.setAge(Integer.valueOf(age));
//    log.info("username={}, age={}", username, age);


    log.info("data={}", data);

    return "modelAttributeV1" + getParamValues(data.getUsername(), data.getAge());
  }

  /**
   * @@ModelAttribute 생략 가능
   * 내가 직접만든 Class 들은 자동으로 spring이 바인딩해서 객체를 생성 해준다.
   */
  @RequestMapping("/model-attribute-v2")
  @ResponseBody // => @RestController와 동일한 효과
  public String modelAttributeV2(
    HelloData data
  ) {
    log.info("data={}", data);

    return "modelAttributeV2" + getParamValues(data.getUsername(), data.getAge());
  }
}
