package hello.proxy.config.v5_autoProxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyFactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <h1>자동 Proxy 생성</h1>
 * <pre>
 *   - Spring Bean 등록된 모든 `Advisor`를 찾아서, 해당되는 method를 가진 Class를 Proxy 적용해서 해당 Proxy Bean을 Spring에 자동 등록해준다.
 *     => Spring에 @BeanPostProcessor (자동 Proxy 생성기)가 이미 Bean으로 등록되어있다.
 *   - Dependency: `aspectJ` (org.springframework.boot:spring-boot-starter-app)
 * </pre>
 */
@Configuration
@Import({AppV1Config.class, AppV2Config.class}) // 예제에서 이미 등록한 interface Class & Concrete Class 을 추가한 Config를 가져온다.
public class AutoProxyConfig {

  @Bean
  public Advisor advisor1(LogTrace logTrace) {
    // pointcut
    NameMatchMethodPointcut pointCut = new NameMatchMethodPointcut();
    pointCut.setMappedNames("request*", "order*", "save*"); // 적용대상 method 패턴을 지정할 수 있다.
    // advice
    LogTraceAdvice advice = new LogTraceAdvice(logTrace);
    return new DefaultPointcutAdvisor(pointCut, advice);
  }
}
