package core.spring.PURE_JAVA;

import core.spring.repository.MemoryMemberRepository;
import core.spring.service.MemberService;
import core.spring.service.MemberServiceImpl;
import core.spring.service.OrderService;
import core.spring.service.OrderServiceImpl;
import core.spring.service.discount.DiscountPolicy;
import core.spring.service.discount.FixDiscountPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @사용목적
 * DI를 통하여 각 Service 의 의존을 끊는다.
 * AppConfig를 통하여 '의존관계'에 대한 고민은 '외부(AppConfig)'에 맡기고,
 * 각 Service는 "실행"에만 집중하도록 한다.
 *
 * @설명
 * appConfig 객체는 MemoryRepository 객체를 샛엉하고, 그 참조값을 memberServiceImpl 생성 시, 생성자로 전달한다.
 */
public class AppConfigByPureJava {

  public MemberService memberService () {
    return new MemberServiceImpl(memberRepository()); // 생성자 주입 (DI)
  }

  public OrderService orderService() {

    return new OrderServiceImpl(memberRepository(), discountPolicy());

  }

  public DiscountPolicy discountPolicy() {
    return new FixDiscountPolicy();
  }

  public MemoryMemberRepository memberRepository() {
    return new MemoryMemberRepository();
  }
}
