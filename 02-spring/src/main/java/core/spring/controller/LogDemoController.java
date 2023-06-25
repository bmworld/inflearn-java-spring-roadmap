package core.spring.controller;

import core.spring.common.MyLogger;
import core.spring.service.LogDemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectFactory;
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


  /**
   * @- @Scope("request") Bean을 바로 주입받으려는 경우, 에러 발생한다. => Why? request scope bean은 HTTP 요청 시점에 생성되기 때문.
   * @- {@link ObjectFactory} 덕분에, HTTP 호출 시점까지  Spring container 에 Bean 요청을 지연가능
   * @- {@link ObjectFactory#getObject()} 호출 시점에는 HTTP 요청이 진행 중이므로, request scope bean 생성이 정상 처리된다.
   * @- `LogDemoController`, `LogDemoService` 등 각기 다른 Class에서 request bean을 따로 호출하더라도,
   *     Spring container는 "같은 HTTP 요청에 대해 동일한 Bean 반환" 한다.
   */
  private final MyLogger logger; // Proxy Ver.

//  private final ObjectProvider<MyLogger> loggerProvider; // Provider Ver.



  private final LogDemoService logDemoService;


  @RequestMapping("/log-demo")
  @ResponseBody
  public String logDemo(HttpServletRequest request) throws InterruptedException {
//    MyLogger logger = loggerProvider.getObject();
    System.out.println("------ proxy class 주입된 logger class = " + logger.getClass());
    String requestURL = request.getRequestURL().toString();
    logger.setRequestURL(requestURL);
    logger.log("controller test");
    Thread.sleep(2000); // UUID가 유지되는지 확인용 => 시간차를 두고, HTTP 요청이 섞이게(리프레쉬 연타) 함
    logDemoService.logic("test id");
    return "OK!!";
  }
}
