package hello.exception;

import hello.exception.filter.LogFilter;
import hello.exception.interceptor.LogInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  /**
   * <h1>Error 중복호출 제거방법: setDispatcherTypes: 필터 태울 DispatcherType 지정</h1>
   * <pre>
   *   - filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR)
   *   => 클라이언트 요청(1) & 오류 페이지 요청(2) 시, 해당 Filter가 호출된다.
   *
   *
   *   * setDisaptcherTypes 기본값은 => DispatcherType.REQUEST 하나만 있음.
   * </pre>
   * <h1>Interceptor 와 중복호출 방지를 위해 Bean 등록해제.</h1>
   */
//  @Bean
  public FilterRegistrationBean logFilter() {
    FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
    filterFilterRegistrationBean.setFilter(new LogFilter());
    filterFilterRegistrationBean.setOrder(1);
    filterFilterRegistrationBean.addUrlPatterns("/*");
    filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR); // 여러 DispatcherType 중, 다음 2가지만 LogFilter가 걸리도록 하기 위함.
//    filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST); // Filter의 DispatcherType 기본값은 REQUEST 만 태운다.
    return filterFilterRegistrationBean;

  }


  /**
   * <h1>Error 중복호출 제거: excludePathPatterns 경로 지정</h1>
   * <pre>
   *   - addPathPatterns & excludePathPatterns => 강력함!
   *     => setDispatcherType을 사용하지 못하는 것을 상쇄한다.
   * </pre>
   * <h2>Interceptor  Error Page 처리 FLOW</h2>
   * <pre>
   *   [ CASE: 정상 호출 ]
   *   - WAS (/error-ex, dispatcherType=REQUEST) -> Filter -> Servlet -> Interceptor -> Controller
   *
   *   [ CASE: 에러 발생 ]
   *   - 1. WAS (여기까지 전파된다!) <- Filter <- Servlet <- Interceptor -> Controller( 에러 발생!)
   *   - 2. WAS -> Error Page 존재여부 확인
   *   - 3. WAS (/error-page/500, dispatcherType=ERROR)
   *        -> Filter(호출 X why? DispatcherType.REQUEST 기본이라는 전제가 있음)
   *        -> Interceptor(excludePatterns 땜시 호출 안됨)
   *        -> Servlet
   *        -> Controller(/error-page/500)
   *        -> View
   *
   * </pre>
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LogInterceptor())
      .order(1)
      .addPathPatterns("/**")
      .excludePathPatterns("/css/**", "*.ico", "/error", "/error-page/**"); // 오류 페이지도 제외시킨다.
  }
}
