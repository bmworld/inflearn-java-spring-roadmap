package hello.proxy.config.v4_porstProcessor;

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

@Slf4j
@Configuration
@Import({AppV1Config.class, AppV2Config.class}) // 예제에서 이미 등록한 interface Class & Concrete Class 을 추가한 Config를 가져온다.
public class BeanPostProcessorConfig {

  private final String basePackage = "hello.proxy.app";

  @Bean
  public PackageLogTracePostProcessor logTracePostProcessor(LogTrace logTrace) {
    return new PackageLogTracePostProcessor(basePackage, getAdvisor(logTrace));
  }


  private Advisor getAdvisor(LogTrace logTrace) {
    // pointcut
    NameMatchMethodPointcut pointCut = new NameMatchMethodPointcut();
    pointCut.setMappedNames("request*", "order*", "save*"); // 적용대상 method 패턴을 지정할 수 있다.
    // advice
    LogTraceAdvice advice = new LogTraceAdvice(logTrace);
    return new DefaultPointcutAdvisor(pointCut, advice);
  }
}
