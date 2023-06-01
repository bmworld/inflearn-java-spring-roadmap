package core.spring;

import core.spring.domain.Grade;
import core.spring.domain.Member;
import core.spring.service.MemberService;
import core.spring.service.OrderService;

public class MemberApp {
  public static void main(String[] args) {
    AppConfig appConfig = new AppConfig();
    MemberService memberService = appConfig.memberService();
    OrderService orderService = appConfig.orderService();

    Long memberId = 1L;
    Member member = new Member(memberId, "member1", Grade.VIP);
    memberService.join(member);

    Member foundMember = memberService.findMember(memberId);
    System.out.println("---- member = " + member);
    System.out.println("---- foundMember = " + foundMember);
  }
}
