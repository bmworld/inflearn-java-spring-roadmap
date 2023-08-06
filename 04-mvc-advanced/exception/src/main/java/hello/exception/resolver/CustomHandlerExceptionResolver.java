package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class CustomHandlerExceptionResolver implements HandlerExceptionResolver {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    try{
      if (ex instanceof CustomException) {
        log.info("CustomExceptionResolver resolver to 400");
        String acceptHandler = request.getHeader("accept");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        if (acceptHandler.equals("application/json")) {
          // CASE: json
          Map<String, Object> errorResult = new HashMap<>();
          errorResult.put("ex", ex.getCause());
          errorResult.put("message", ex.getMessage());

          String resultAsString = objectMapper.writeValueAsString(errorResult);

        response.setContentType("application/json");
          response.setCharacterEncoding("UTF-8");
          response.getWriter().write(resultAsString);
          return new ModelAndView(); // Exception를 WAS로 올리지 않고, "정상흐름"으로써 처리한다.
        } else {
          // case: TEXT/HTML
          return new ModelAndView("error/500"); // template/error/500 호출시킴

        }
      }
    } catch(IOException e) {
      log.error("CustomHandlerExceptionResolver ex", ex);
    }

    return null; // null 반환 시, 기존 Exception 처리 로직 그대로 진행됨.
  }
}
