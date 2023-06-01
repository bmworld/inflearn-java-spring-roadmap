package core.spring.config;

import core.spring.repository.MemoryMemberRepository;
import core.spring.service.MemberService;
import core.spring.service.MemberServiceImpl;
import core.spring.service.OrderService;
import core.spring.service.OrderServiceImpl;
import core.spring.service.discount.DiscountPolicy;
import core.spring.service.discount.RateDiscountPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * <h2>기존에는 개발자가 직접 자바코드로 모든 것을 컨트롤함 (제어의 주도권)</h2>
 * <h2>이제 IoC 를 적용(제어의 역전)</h2>
 * Spring Container에 객체를 Spring Bean으로 등록하고,
 * SpringContainer 에서 Spring Bean을 찾아서 사용하도록 변경
 * @사용목적
 * DI를 통하여 각 Service 의 의존을 끊는다.
 * AppConfig를 통하여 '의존관계'에 대한 고민은 '외부(AppConfig)'에 맡기고,
 * 각 Service는 "실행"에만 집중하도록 한다.
 *
 * @설명
 * appConfig 객체는 MemoryRepository 객체를 샛엉하고, 그 참조값을 memberServiceImpl 생성 시, 생성자로 전달한다.
 */
@Configuration // 애플리케이션 구성정보 담당
public class AppConfig {

  @Bean(name = "renamed_discountPolicy") // Spring Container 에 등록할 이름 지정가능 / But, 왠만하면 그냥 쓰시라.
  public DiscountPolicy discountPolicy() {
    return new RateDiscountPolicy();
  }

  @Bean  // Spring Container 의 관리대상이 된다.
  public MemberService memberService () {
    return new MemberServiceImpl(memberRepository()); // 생성자 주입 (DI)
  }

  @Bean
  public OrderService orderService() {

    return new OrderServiceImpl(memberRepository(), discountPolicy());

  }

  @Bean
  public MemoryMemberRepository memberRepository() {
    return new MemoryMemberRepository();
  }
}
