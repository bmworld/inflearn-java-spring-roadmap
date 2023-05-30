package core.spring.service;

import core.spring.domain.Grade;
import core.spring.domain.Member;
import core.spring.domain.Order;
import core.spring.repository.MemberRepository;
import core.spring.repository.MemoryMemberRepository;
import core.spring.service.discount.DiscountPolicy;
import core.spring.service.discount.FixDiscountPolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

  private final MemberService memberService = new MemberServiceImpl();
  private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
  private final OrderService orderService = new OrderServiceImpl();
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
