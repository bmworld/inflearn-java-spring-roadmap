package hello.exception.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class ServletExceptionController {

  @GetMapping("/error-ex")
  public void errorException() {
    throw new RuntimeException("Error occurred!");
  }


  /**
   * <h1>response.sendError()</h1>
   * <pre>
   *   - HttpServletResponse 내부에 오류가 발생한 상태를 지정하고, 저장한다.
   *   - ServletContainer는 사용자에게 응답 전에 response 내에 sendError() 호출되었는지 확인
   *   - 호출되었다면, 설정한 오류 코드에맞추어 기본 Error Page를 보여준다.
   * </pre>
   */
  @GetMapping("/error-404")
  public void error404(HttpServletResponse response) throws IOException {
    response.sendError(404, "404 Error occurred!");
  }

  @GetMapping("/error-500")
  public void error500(HttpServletResponse response) throws IOException {
    response.sendError(500, "500 Internal Server Error!");
  }
}
