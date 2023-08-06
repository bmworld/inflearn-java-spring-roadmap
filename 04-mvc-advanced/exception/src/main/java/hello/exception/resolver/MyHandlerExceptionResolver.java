package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    log.info("Call Resolver = ", ex);


    try {
      if (ex instanceof IllegalArgumentException) {
        log.info("IllegalArgumentException resolver to 400");
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
        return new ModelAndView(); // 빈값으로 ModelAndView Return 시, WAS까지 정상적으로 sendError가. 전달된다.
      }

    } catch (IOException e) {
      log.error("resolver ex", e);
    }

    return null; // null return 시, 기본 예외가 그대로 WAS로 전달됨.
  }
}
