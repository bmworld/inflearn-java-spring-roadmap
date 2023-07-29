package hello.login.web.filter;


import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

import static java.time.LocalDateTime.now;

/**
 * 참고: 실무에서 HTTP 요청 시, 같으 ㄴ요청의 로그에 모두 같은 식별자를 자동으로 남기는 방법 => logback mdc
 */
@Slf4j
public class LogFilter implements Filter {
  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
//    Filter.super.init(filterConfig);
    log.info("Log filter initialized");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

    log.info("Log filter doFilter");
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String requestURI = httpRequest.getRequestURI();
    String uuid = UUID.randomUUID().toString();
    long beforeTime = System.currentTimeMillis();
    try{
      log.info("REQUEST [{}] / URI=[{}], timestamp=[{}]", uuid, requestURI, now());


      // ! 반드시 doFilter 사용 (why? "filter chain" 넘겨줘야, 로직이 끊기지 않음)
      chain.doFilter(request, response);
    } catch(Exception e) {
      log.info("Error processing = {}", e.getMessage());
//      e.printStackTrace();
      throw e;
    } finally {
      log.info("REQUEST [{}] / URI=[{}] / timestamp=[{}] / takenMilliTime=[{}] ", uuid, requestURI, now(), getTakenMilliTime(beforeTime));

    }


  }

  private String getTakenMilliTime(long beforeTime) {
    long afterTime = System.currentTimeMillis();
    return String.valueOf(afterTime - beforeTime );
  }

  @Override
  public void destroy() {
//    Filter.super.destroy();
    log.info("Log filter destroy");
  }
}
