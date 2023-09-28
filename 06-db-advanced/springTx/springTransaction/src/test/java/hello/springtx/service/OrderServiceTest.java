package hello.springtx.service;

import hello.springtx.exception.NotEnoughMoneyException;
import hello.springtx.order.Order;
import hello.springtx.order.OrderStatus;
import hello.springtx.order.testUserName;
import hello.springtx.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Slf4j
class OrderServiceTest {

  @Autowired
  private OrderService orderService;

  @Autowired
  private OrderRepository orderRepository;

  @Test
  @DisplayName("Order 비즈니스 로직 검증")
  public void complete() throws Exception {
    // Given
    Order order = new Order();
    order.setUsername(testUserName.normalUserName);

    // When
    orderService.order(order);

    // Then
    Order foundOrder = orderRepository.findById(order.getId()).orElseThrow();
    assertThat(foundOrder.getOrderStatus()).isEqualTo(OrderStatus.PAYMENT_DONE);

  }

  @Test
  @DisplayName("Order 시스템 오류 => Rollback")
  public void runtimeExWithRollback() {
    // Given
    Order order = new Order();
    order.setUsername(testUserName.systemExUserName);

    // When
    Assertions.assertThatThrownBy(()-> orderService.order(order))
      .isInstanceOf(RuntimeException.class);

    // Then
    Optional<Order> optionalOrder = orderRepository.findById(order.getId());
    assertThat(optionalOrder).isEmpty();

  }



  @Test
  @DisplayName("Order 비즈니스 오류 => Commit")
  public void bizExWithCommit() {
    // Given
    Order order = new Order();
    order.setUsername(testUserName.businessExUserName);

    // When => 고객에게 잔고 부족을 알리고 별도의 계좌 입금 안내
    Assertions.assertThatThrownBy(()-> orderService.order(order))
      .isInstanceOf(NotEnoughMoneyException.class);

    // Then
    Order foundOrder = orderRepository.findById(order.getId()).orElseThrow();
    assertThat(foundOrder.getOrderStatus()).isEqualTo(OrderStatus.BEFORE_PAYMENT);

  }
}
