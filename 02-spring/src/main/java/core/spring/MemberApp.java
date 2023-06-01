package core.spring;

import core.spring.config.AppConfig;
import core.spring.domain.Grade;
import core.spring.domain.Member;
import core.spring.service.MemberService;
import core.spring.service.discount.DiscountPolicy;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


public class MemberApp {
  public static void main(String[] args) {
    AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(AppConfig.class); // Spring Container
    MemberService memberService = appContext.getBean("memberService", MemberService.class); // getBean(Bean 이름, 반환 Type) SpringContainer 에는 기본적으로 Method 이름으로 등록된다.
    DiscountPolicy discountPolicy = appContext.getBean("renamed_discountPolicy", DiscountPolicy.class); // getBean(Bean 이름, 반환 Type) SpringContainer 에는 기본적으로 Method 이름으로 등록된다.

    System.out.println("-----Bean 이름을 변경한 discountPolicy = " + discountPolicy);


    Long memberId = 1L;
    Member member = new Member(memberId, "member1", Grade.VIP);
    memberService.join(member);

    Member foundMember = memberService.findMember(memberId);
    System.out.println("---- member = " + member);
    System.out.println("---- foundMember = " + foundMember);
  }
}
