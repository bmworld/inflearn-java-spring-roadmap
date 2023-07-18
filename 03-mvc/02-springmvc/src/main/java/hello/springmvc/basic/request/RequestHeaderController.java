package hello.springmvc.basic.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@RestController
@RequestMapping
@Slf4j
public class RequestHeaderController {

  @RequestMapping("headers")
  public String headers(HttpServletRequest req,
                        HttpServletResponse res,
                        HttpMethod httpMethod,
                        Locale locale,
                        @RequestHeader MultiValueMap<String, String> headerMap, // 모든 헤더값 받아옴
                        @RequestHeader("host") String host, // 특정 헤더값 받아옴
                        @CookieValue(value = "myCookie", required = false) String cookie


  ) {

    log.info("request={}", req);
    log.info("response={}", res);
    log.info("httpMethod={}", httpMethod);
    log.info("locale={}", locale);
    log.info("headerMap={}", headerMap);
    log.info("header host={}", host);
    log.info("myCookie={}", cookie);

    return "OK!";
  }

}
