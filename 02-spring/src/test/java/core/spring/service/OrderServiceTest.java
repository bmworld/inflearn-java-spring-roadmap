package core.spring.service;

import core.spring.config.AppConfig;
import core.spring.domain.Grade;
import core.spring.domain.Member;
import core.spring.domain.Order;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

  private MemberService memberService;
  private OrderService orderService;


  @BeforeEach
  public void beforeEach() throws Exception {
    AppConfig appConfig = new AppConfig();
    this.memberService = appConfig.memberService();
    this.orderService = appConfig.orderService();
  }

  @Test
  @DisplayName("orderTest")
  public void orderTest() throws Exception{
    // Given, When
    Long memberId = 1L;
    Member member = new Member(memberId, "m1", Grade.VIP);
    memberService.join(member);

    Order order = orderService.createOrder(memberId, "itemA", 10000);

    // Then
    Assertions.assertThat(order.getDiscountPrice()).isEqualTo(1000);

  }

}
