package hello.proxy.config.v3_proxyFactory;

import hello.proxy.app.v1.*;
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
 * <h1>스프링 제공 ProxyFactory: `interface` 존재 시 `JDK 동적 Proxy` 사용 & Spring 제공 Advisor 사용하여 등록</h1>
 */
@Slf4j
@Configuration
public class ProxyFactoryConfigV1 {

  @Bean
  public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
    OrderControllerV1 target = new OrderControllerV1Impl(orderServiceV1(logTrace));
    ProxyFactory factory = new ProxyFactory(target);
    factory.addAdvisor(getAdvisor(logTrace));
    OrderControllerV1 proxy = (OrderControllerV1) factory.getProxy();
    log.info("--- orderControllerV1 > ProxyFactory proxy={}, target={}", proxy.getClass(), target.getClass());
    return proxy;
  }

  @Bean
  public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
    OrderServiceV1 target = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
    ProxyFactory factory = new ProxyFactory(target);
    factory.addAdvisor(getAdvisor(logTrace));
    OrderServiceV1 proxy = (OrderServiceV1) factory.getProxy();
    log.info("--- orderServiceV1 > ProxyFactory proxy={}, target={}", proxy.getClass(), target.getClass());
    return proxy;
  }

  @Bean
  public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
    OrderRepositoryV1Impl target = new OrderRepositoryV1Impl();
    ProxyFactory factory = new ProxyFactory(target);
    factory.addAdvisor(getAdvisor(logTrace));
    OrderRepositoryV1 proxy = (OrderRepositoryV1) factory.getProxy();
    log.info("--- orderRepositoryV1 > ProxyFactory proxy={}, target={}", proxy.getClass(), target.getClass());
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
