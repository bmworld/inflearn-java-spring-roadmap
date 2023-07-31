package hello.login.config;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;


@Configuration
public class WebConfig implements WebMvcConfigurer {

  /**
   * `filter`보다 `interceptor`가 더 편리한 기능을 많이 제공한다.
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 실행 순서 주의
    registry.addInterceptor(new LogInterceptor())
      .order(1)
      .addPathPatterns("/**")
      .excludePathPatterns("/css/**", "/*.ico", "/error")
    ;

    // 실행 순서 주의
    // addPathPatterns, excludePathPatterns 사용하여서,  세밀하게 경로에 대한 interceptor를 적용할 수 있다.
    registry.addInterceptor(new LoginCheckInterceptor())
      .order(2)
      .addPathPatterns("/**")
      .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.icon", "/error")
    ;

  }


//  @Bean
  public FilterRegistrationBean logFilter() {
    FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
    filterFilterRegistrationBean.setFilter(new LogFilter());
    filterFilterRegistrationBean.setOrder(1);

    String allURLPattern = "/*";
    filterFilterRegistrationBean.addUrlPatterns(allURLPattern);

    return filterFilterRegistrationBean;

  }

//  @Bean
  public FilterRegistrationBean loginCheckFilter() {
    FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
    filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
    filterFilterRegistrationBean.setOrder(2);

    String allURLPattern = "/*";
    filterFilterRegistrationBean.addUrlPatterns(allURLPattern);

    return filterFilterRegistrationBean;
  }
}
