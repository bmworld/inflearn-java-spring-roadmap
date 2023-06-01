package core.spring;

import core.spring.config.AppConfig;
import core.spring.domain.Grade;
import core.spring.domain.Member;
import core.spring.domain.Order;
import core.spring.service.MemberService;
import core.spring.service.OrderService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class); // Spring Container
    MemberService memberService = appContext.getBean("memberService", MemberService.class); // getBean(Bean 이름, 반환 Type) SpringContainer 에는 기본적으로 Method 이름으로 등록된다.
    OrderService orderService = appContext.getBean("orderService", OrderService.class);

    Long memberId = 1L;
    Member member = new Member(memberId, "member1", Grade.VIP);
    memberService.join(member);

    Order order = orderService.createOrder(memberId, "itemA", 20000);
    System.out.println("order = " + order);
  }
}
