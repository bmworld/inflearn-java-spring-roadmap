package hello.proxy.config.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.config.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.config.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;

/**
 * <h1>Proxy 적용하기 -> Spring Bean으로서 ( 실제 객체 대신 ) Proxy 객체 등록 </h1>
 * <pre>
 *   - Spring Bean으로 실체 객체 대신 Proxy가 등록되었으므로, 향후 Spring Bean을 주입받을 때는
 *     `Proxy` 객체가 주입된다.
 *   - Proxy 객체가 실객체를 참조하므로, `Proxy를 통하여 실체 객체를 호출 가능`
 *
 *   - Proxy 객체는 Spring Container가 관리하고, Heap Memory에 올라감.
 *   - 실제 객체는 Java Heap Memory에 올라감 / But Spring Container 관리 대상 X
 * </pre>
 */
@Configurable
public class InterfaceProxyConfig {
  @Bean
  public OrderControllerV1 orderController(LogTrace logTrace) {
    OrderControllerV1Impl target = new OrderControllerV1Impl(orderService(logTrace));
    return new OrderControllerInterfaceProxy(target, logTrace);
  }

  @Bean
  public OrderServiceV1 orderService(LogTrace logTrace) {
    OrderServiceV1Impl target = new OrderServiceV1Impl(orderRepository(logTrace));
    return new OrderServiceInterfaceProxy(target, logTrace);
  }

  @Bean
  public OrderRepositoryV1 orderRepository(LogTrace logTrace) {
    OrderRepositoryV1Impl target = new OrderRepositoryV1Impl();
    return new OrderRepositoryInterfaceProxy(target, logTrace);
  }
}
