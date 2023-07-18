package hello.springmvc.basic.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyJsonController {

  private ObjectMapper om = new ObjectMapper();


  private String getBodyString(HttpServletRequest req, String body) {
    String uri = req.getRequestURI();
    return "--- uri = " + uri +  " / json body = " + body.toString();
  }


  @PostMapping("/request-body-json-v1")
  @ResponseBody
  public String requestBodyJsonV1(
    HttpServletRequest req,
    HttpServletResponse res
  ) throws IOException {


    ServletInputStream inputStream = req.getInputStream(); // body를 가져온다.
    String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("body={}", body);
    om.readValue(body, HelloData.class);
    return getBodyString(req,  body);
  }


  @PostMapping("/request-body-json-v2")
  @ResponseBody
  public String requestBodyJsonV2(
    HttpServletRequest req,
    @RequestBody String body
  ) throws IOException {

    log.info("body={}", body);
    om.readValue(body, HelloData.class);
    return getBodyString(req,  body);
  }


  /**
   * @RequestBody 생략하면 안 됨 <br/>
   *  WHY?<br/>
   *  - 단순 타입 (String, int, Integer):  @RequestParam 붙음 <br/>
   *  -  그 외 Type : @ModelAttribute 붙음.
   *  <br/>
   *  <br/>
   *  <br/>
   *
   *  @주의
   *  <p>HTTP요청 시, application/json 인지 반드시 확인하시라.</p>
   *  <p>그래야만 JSON 처리가능한 HTTP Converter가 실행됨.</p>
   */

  @PostMapping("/request-body-json-v3")
  @ResponseBody
  public String requestBodyJsonV3(
    HttpServletRequest req,
    @RequestBody HelloData data
  ) throws IOException {

    log.info("body={}", data);
    String body = om.writeValueAsString(data);
    return getBodyString(req,  body);
  }



  @PostMapping("/request-body-json-v4")
  @ResponseBody
  public String requestBodyJsonV4(
    HttpServletRequest req,
    HttpEntity<HelloData> httpEntity
  ) throws IOException {

    HelloData data = httpEntity.getBody();

    log.info("body={}", data);
    String body = om.writeValueAsString(data);
    return getBodyString(req,  body);
  }


  /**
   * @Request_Body_요청 JSON 요청 -> HTTP Message Converter -> 객체
   * @ResponseBody_응답 객체 -> HTTP  Message Converter -> JSON 응답
   */
  @PostMapping("/request-body-json-v5")
  @ResponseBody
  public HelloData requestBodyJsonV5(
    HttpServletRequest req,
    @RequestBody HelloData data
  ) throws IOException {

    log.info("body={}", data);
    String body = om.writeValueAsString(data);
    return data;
  }
}
