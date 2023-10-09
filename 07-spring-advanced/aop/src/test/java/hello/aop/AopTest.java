package hello.aop;

import hello.aop.order.OrderConst;
import hello.aop.order.OrderRepository;
import hello.aop.order.OrderService;
import hello.aop.order.aop.AspectV4Pointcut;
import hello.aop.order.aop.AspectV5SetAspectOrder;
import hello.aop.order.aop.AspectV6Advice;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
//@Import(AspectV1.class)
//@Import(AspectV2.class)
//@Import(AspectV3.class)
//@Import(AspectV4Pointcut.class)
//@Import({AspectV5SetAspectOrder.LogAspect.class, AspectV5SetAspectOrder.TxAspect.class})
@Import(AspectV6Advice.class)
public class AopTest {
  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderRepository orderRepository;

  @DisplayName("Aop 적용여부 조회")
  @Test
  void getAopInfo() {
    log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderRepository));
    log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderService));
  }

  @DisplayName("Aop 적용여부 확인: Success")
  @Test
  void successCASE () {
    orderService.orderItem("itemA");

  }


  @DisplayName("Aop 적용여부 확인: Exception")
  @Test
  void exceptionCASE () {
    Assertions.assertThatThrownBy(() -> orderService.orderItem(OrderConst.exceptionItemName))
      .isInstanceOf(IllegalStateException.class);

  }
}
