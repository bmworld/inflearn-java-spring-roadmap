package hello.proxy.config.v2_dynamicProxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v2_dynamicProxy.handler.LogTraceBasicHandler;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.java.Log;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyBasicConfig {

  @Bean
  public OrderControllerV1 orderControllerV1(LogTrace logTrace) {
    OrderControllerV1 target = new OrderControllerV1Impl(orderServiceV1(logTrace));
    return (OrderControllerV1) Proxy.newProxyInstance(OrderControllerV1.class.getClassLoader(), new Class[]{OrderControllerV1.class}, new LogTraceBasicHandler(target, logTrace));
  }

  @Bean
  public OrderServiceV1 orderServiceV1(LogTrace logTrace) {
    OrderServiceV1 target = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
    return (OrderServiceV1) Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(), new Class[]{OrderServiceV1.class}, new LogTraceBasicHandler(target, logTrace));
  }

  @Bean
  public OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
    OrderRepositoryV1 target = new OrderRepositoryV1Impl();
    return (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(), new Class[]{OrderRepositoryV1.class}, new LogTraceBasicHandler(target, logTrace));
  }
}
