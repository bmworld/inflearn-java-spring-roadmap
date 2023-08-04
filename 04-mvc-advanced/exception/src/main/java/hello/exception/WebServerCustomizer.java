package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


 /**
  * <h1>핵심</h1>
  * <pre>
  *   1. 예외 발생 시, WAS까지 전파된다.
  *   2. WAS는 오류 페이지 존재 경로를 찾아서 내부 적으로오류 페이지를 호출.
  *   ( 이때, Filter, Servlet, Interceptor, Controller 모두 다.시.호.출.된.다.
  * </pre>
  *
 * <h2>예외 발생 흐름</h2>
 * <pre>
 *   - WAS (여기까지 전파됨) <- Filter <- Servlet <- Interceptor <- Controller (Exception occurred)
 * </pre>
 * <h2>ServletResponse > sendError 흐름</h2>
 * <pre>
 *   - WAS (sendError 호출 기록 확인) <- Filter <- Servlet <- Interceptor <- Controller (response.sendError())
 * </pre>
 * <h2> WAS 는 해당 예외를 처리하는 Error Page 정보 확인</h2>
 * <pre>
 *   - new ErrorPage(RuntimeException.class, "error-page/500")
 *
 *   - WAS > ErrorPage 출력을 위해, WAS에 "error-page/500" Request 처음부터 다시 날림
 * </pre>
 * <h2>오류페이지 요청 흐름</h2>
 * <pre>
 *   WAS -> "/error-page/500" 자체적으로 Request 생성 -> Filter -> Servlet 생성 -> Interceptor -> Controller ("/error-page/500") -> View
 * </pre>
 */
//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

  /**
   * <h1>오류 페이지 예외 참조</h1>
   * <pre>
   *   * 오류페이지는 예외를 다룰 때 해당 예외의 그 자식 타입의 오류를 함께 처리한다.
   *   ex. `RuntimeException` & Child Class of RuntimeException
   * </pre>
   */
  @Override
  public void customize(ConfigurableWebServerFactory factory) {
    // Spring 제공하는 Custom error page


    // 특정 HTTP Error 발생 시, 지정된 Controller Request => ( Path Custom Error Page 처리할 수 있다.~)
    ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
    ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
    ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

    factory.addErrorPages(errorPage404, errorPage500, errorPageEx);


  }
}
