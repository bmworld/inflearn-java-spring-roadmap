package hello.login.web.argumentResolver;

import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

  /**
   * <h1>Parameter Annotation 지원여부 판별</h1>
   */
  @Override
  public boolean supportsParameter(MethodParameter parameter) {

    log.info("MemberArgumentResolver > supportsParameter 실행");

    boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
    boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

    return hasLoginAnnotation && hasMemberType;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
    log.info("MemberArgumentResolver > resolveArgument ");
    HttpServletRequest req = (HttpServletRequest)webRequest.getNativeRequest();
    HttpSession session = req.getSession(false);


    if (session == null) return null;

    // Session에 Member 들어있을 경우.
    return session.getAttribute(SessionConst.LOGIN_MEMBER);
  }
}
