package hello.login.web.interceptor;

import hello.login.web.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <h1>filter와 차이점</h1>
 *  => 특정 auth path를 interceptor  등록 시에, 전부 처리 가능하다!!
 *  <pre>
 *
 *    {@code
 *      public class WebConfig implements WebMvcConfigurer { ...
 *        registry.addInterceptor(new LoginCheckInterceptor())
 *          .order(2)
 *          .addPathPatterns("/**")
 *          .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.icon", "/error");
 *      }
 *    }
 *
 *  </pre>
 */
@Slf4j
@Component
public class LoginCheckInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String requestURI = request.getRequestURI();
    log.info("LoginCheckInterceptor => {}", requestURI);

    HttpSession session = request.getSession();
    if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
      log.info("LoginCheckInterceptor => 미인증 사용자 요청");
      // Redir to login page
      response.sendRedirect("/login?redirectURI="+ requestURI);
      return false;
    }
    return true;
  }
}
