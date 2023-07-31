package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
@Component
public class LogInterceptor implements HandlerInterceptor {

  public static final String LOG_ID = "logId";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String requestURI = request.getRequestURI();
    String uuid = UUID.randomUUID().toString();

    request.setAttribute(LOG_ID, uuid);

    if (handler instanceof HandlerMethod) {
      log.info("LogInterceptor Request: [{}] / [{}] / [{}] ", uuid, requestURI, handler);
      HandlerMethod hm = (HandlerMethod) handler; // 호출예정인 Controller Method의 모든 정보가 포함되어 있다.
    }


    log.info("LogInterceptor Request: [{}] / [{}] / [{}] ", uuid, requestURI, handler);


    // return false; => 다음 단계가 진행 안되고, 로직을 끝낸다 (Controller 호출 안된다.)
    return true; // true 시, 다음 단계인 Controller 호출된다


  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    log.info("LogInterceptor > postHandler [{}]", modelAndView);
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    String requestURI = request.getRequestURI();
    Object logId = request.getAttribute(LOG_ID);
    log.info("LogInterceptor Request: [{}] / [{}] / [{}] ", logId, requestURI, handler);
    if (ex != null) {
      log.error("LogInterceptor After Completion > error =>", ex); // Error는 log 호출 시, {} 없어도 된다.
    }

  }
}
