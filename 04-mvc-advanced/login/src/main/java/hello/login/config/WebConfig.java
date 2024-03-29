package hello.login.config;

import hello.login.web.argumentResolver.MemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;
import java.util.List;


@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
  private final LogInterceptor logInterceptor;
  private final LoginCheckInterceptor loginCheckInterceptor;
  private final LoginCheckFilter loginCheckFilter;
  private final LogFilter logFilter;


  /**
   * <h1>ArgumentResolver -> Custom Annotation 등록하기</h1>
   */
  @Override

  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(new MemberArgumentResolver());
  }

  /**
   * `filter`보다 `interceptor`가 더 편리한 기능을 많이 제공한다.
   */
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    // 실행 순서 주의
    registry.addInterceptor(logInterceptor)
      .order(1)
      .addPathPatterns("/**")
      .excludePathPatterns("/css/**", "/*.ico", "/error")
    ;

    // 실행 순서 주의
    // addPathPatterns, excludePathPatterns 사용하여서,  세밀하게 경로에 대한 interceptor를 적용할 수 있다.
    registry.addInterceptor(loginCheckInterceptor)
      .order(2)
      .addPathPatterns("/**")
      .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.icon", "/error")
    ;

  }


  //  @Bean
  public FilterRegistrationBean logFilter() {
    FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
    filterFilterRegistrationBean.setFilter(logFilter);
    filterFilterRegistrationBean.setOrder(1);

    String allURLPattern = "/*";
    filterFilterRegistrationBean.addUrlPatterns(allURLPattern);

    return filterFilterRegistrationBean;

  }

  //  @Bean
  public FilterRegistrationBean loginCheckFilter() {
    FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
    filterFilterRegistrationBean.setFilter(loginCheckFilter);
    filterFilterRegistrationBean.setOrder(2);

    String allURLPattern = "/*";
    filterFilterRegistrationBean.addUrlPatterns(allURLPattern);

    return filterFilterRegistrationBean;
  }
}
