package hello.exception;

import hello.exception.filter.LogFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  /**
   * <h1>setDispatcherTypes: 필터 태울 DispatcherType 지정!</h1>
   * <pre>
   *   - filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR)
   *   => 클라이언트 요청(1) & 오류 페이지 요청(2) 시, 해당 Filter가 호출된다.
   *
   *
   *   * setDisaptcherTypes 기본값은 => DispatcherType.REQUEST 하나만 있음.
   * </pre>
   * @return
   */
  @Bean
  public FilterRegistrationBean logFilter() {
    FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
    filterFilterRegistrationBean.setFilter(new LogFilter());
    filterFilterRegistrationBean.setOrder(1);
    filterFilterRegistrationBean.addUrlPatterns("/*");
    filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ERROR); // 여러 DispatcherType 중, 다음 2가지만 LogFilter가 걸리도록 하기 위함.
//    filterFilterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST); // Filter의 DispatcherType 기본값은 REQUEST 만 태운다.
    return filterFilterRegistrationBean;

  }

}
