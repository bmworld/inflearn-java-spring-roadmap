package hello.springmvc.basic.request;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Controller
public class RequestBodyStringController {
  private String getBodyString(String body) {
    return "/ body = " + body.toString();
  }

  private String getParamValues(String username, Integer age) {
    return "----- Param values => username= " + username + "/ age = " + age;
  }

  @RequestMapping("/request-body-string-v1")
  public String requestBodyStringV1(
    HttpServletRequest req,
    HttpServletResponse res
  ) throws IOException {
    ServletInputStream inputStream = req.getInputStream(); // body를 가져온다.
    String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("body={}", body);
    return "requestBodyStringV1" + getBodyString(body);
  }


  @RequestMapping("/request-body-string-v2")
  public void requestBodyStringV2(
    InputStream inputStream, Writer responseWriter
  ) throws IOException {
    String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

    log.info("body={}", body);
    responseWriter.write(body);
  }


  /**
   * @HttpEntity HTTP header, body를 편하게 조회할 수 있게 만들어진 객체 <br/>
   * 문자면, HTTP BODY 내용을 문자로 변환해서 돌려줌 <br/>
   * HTTP Message Converter가 동작한다. <br/>
   * InputStream 처리 등을 한바에 처리해줌.
   *
   * @RequestEntity : HttpEntity 상속받은 HttpRequest 정보를 담은 객체
   * @ResponseEntity : HttpEntity 상속받고, HttpResponse 정보를 담아서 전달할 수 있는 객체.
   */
  @RequestMapping("/request-body-string-httpEntity")
  public HttpEntity<String> requestBodyStringV3(
    RequestEntity<String> httpEntity
  ) {

    String body = httpEntity.getBody();

    log.info("body={}", body);
    return new ResponseEntity<String>(body, HttpStatus.CREATED);
  }

  @RequestMapping("/request-body-string-by-annotation-v1")
  public HttpEntity<String> requestBodyByRequestBodyAnnotation(
    @RequestBody String messageBody
  ) {

    log.info("body={}", messageBody);
    return new ResponseEntity<String>(messageBody, HttpStatus.CREATED);
  }


  /**
   * @ResponseBody HTTP Body정보를 편리하게 조회할 수 있다.
   * @HttpEntity header 정보가 필요한 경우 사용
   * @RequestHeader header 정보가 필요한 경우 사용
   */

  @RequestMapping("/request-body-string-by-annotation-v2")
  @ResponseBody
  public String requestBodyByRequestBodyAndResponseBodyAnnotation(
    @RequestBody String messageBody
  ) {

    log.info("body={}", messageBody);
    return messageBody;
  }
}
