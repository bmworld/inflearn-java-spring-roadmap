package core.spring.controller;

import core.spring.common.MyLogger;
import core.spring.service.LogDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <h1>logger 정상 작동여부 테스트를 위한 Controller</h1>
 * @- request: HttpServletRequest를 통해 요청 URL을 받는다.
 * @- 위 requestURL은, logger에 저장한다
 * @- Request는 HTTP 요청 각각을 구분하므로 다른 HTTP 요청과 섞일 걱정은 없다 (UUID까지 부여했으니, 그것으로 확인하면 된다)
 * <br/>
 * <br/>
 * <br/>
 * <br/>
 *   @[참고] 해당 Log는 공통 처리가 가능한 `Spring Interceptor` 또는 `Servlet Filter`를 활용하시라.
 *          해당 예제는 예제의 단순화를 위해 Controller 에서 진행하는 것이다.
 *
 */
@RestController
@RequiredArgsConstructor
public class LogDemoController {

  private final LogDemoService logDemoService;
  private final MyLogger logger; // request scope => client request가 있어야 존재하는데, 요청이 없으니 최초 주입 시 에러가 발생한다.
  // 따라서 이때, Provider를 사용해야하는 거다.


  @RequestMapping("/log-demo")
  @ResponseBody
  public String logDemo(HttpServletRequest request) {
    String requestURL = request.getRequestURL().toString();
    logger.setRequestURL(requestURL);
    logger.log("controller test");
    logDemoService.logic("test id");
    return "OK!!";
  }
}
