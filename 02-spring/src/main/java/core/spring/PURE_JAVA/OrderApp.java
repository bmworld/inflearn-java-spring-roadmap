package core.spring.PURE_JAVA;

import core.spring.domain.Grade;
import core.spring.domain.Member;
import core.spring.domain.Order;
import core.spring.service.MemberService;
import core.spring.service.OrderService;

public class OrderApp {
  public static void main(String[] args) {
    AppConfigByPureJava appConfig = new AppConfigByPureJava();
    MemberService memberService = appConfig.memberService();
    OrderService orderService = appConfig.orderService();

    Long memberId = 1L;
    Member member = new Member(memberId, "member1", Grade.VIP);
    memberService.join(member);

    Order order = orderService.createOrder(memberId, "itemA", 10000);
    System.out.println("order = " + order);
  }
}
