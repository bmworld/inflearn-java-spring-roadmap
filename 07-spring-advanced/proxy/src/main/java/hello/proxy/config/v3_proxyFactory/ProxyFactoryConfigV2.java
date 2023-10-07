package hello.proxy.config.v3_proxyFactory;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.config.v3_proxyFactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h1>`구체 클래스` 사용 시 `CGLIB Proxy` 사용 & Spring 제공 Advisor 사용하여 등록</h1>
 */
@Slf4j
@Configuration
public class ProxyFactoryConfigV2 {

  @Bean
  public OrderControllerV2 orderControllerV2(LogTrace logTrace) {
    OrderControllerV2 target = new OrderControllerV2(orderServiceV2(logTrace));
    ProxyFactory factory = new ProxyFactory(target);
    factory.addAdvisor(getAdvisor(logTrace));
    OrderControllerV2 proxy = (OrderControllerV2) factory.getProxy();
    log.info("--- OrderControllerV2 > ProxyFactory proxy={}, target={}", proxy.getClass(), target.getClass());
    return proxy;
  }

  @Bean
  public OrderServiceV2 orderServiceV2(LogTrace logTrace) {
    OrderServiceV2 target = new OrderServiceV2(orderRepositoryV2(logTrace));
    ProxyFactory factory = new ProxyFactory(target);
    factory.addAdvisor(getAdvisor(logTrace));
    OrderServiceV2 proxy = (OrderServiceV2) factory.getProxy();
    log.info("--- OrderServiceV2 > ProxyFactory proxy={}, target={}", proxy.getClass(), target.getClass());
    return proxy;
  }

  @Bean
  public OrderRepositoryV2 orderRepositoryV2(LogTrace logTrace) {
    OrderRepositoryV2 target = new OrderRepositoryV2();
    ProxyFactory factory = new ProxyFactory(target);
    factory.addAdvisor(getAdvisor(logTrace));
    OrderRepositoryV2 proxy = (OrderRepositoryV2) factory.getProxy();
    log.info("--- OrderRepositoryV2 > ProxyFactory proxy={}, target={}", proxy.getClass(), target.getClass());
    return proxy;
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
