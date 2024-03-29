package core.spring.scan;

import core.spring.DI.AutowiredAtField;
import core.spring.DI.AutowiredAtFieldNonTarget;
import core.spring.DI.AutowiredAtFieldTarget;
import core.spring.config.AutoAppConfig;
import core.spring.repository.MemoryMemberRepository;
import core.spring.service.MemberService;
import core.spring.service.OrderService;
import core.spring.service.OrderServiceImpl;
import core.spring.service.discount.RateDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;


public class AutoAppConfigTest {

  @Test
  @DisplayName("@ComponentScan 테스트 - @Component가 붙은 Class를 자동으로 Spring Bean으로 등록한다.")
  public void basicComponentScanTest() throws Exception {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

    String[] beanDefinitionNames = ac.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
      System.out.println("beanDefinitionName = " + beanDefinitionName);
    }


    MemberService memberService = ac.getBean(MemberService.class);
    assertThat(memberService).isInstanceOf(MemberService.class);

    RateDiscountPolicy rateDiscountPolicy = ac.getBean(RateDiscountPolicy.class);
    assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);

    MemoryMemberRepository memoryMemberRepository = ac.getBean(MemoryMemberRepository.class);
    assertThat(memoryMemberRepository).isInstanceOf(MemoryMemberRepository.class);

    OrderService orderService = ac.getBean(OrderService.class);
    assertThat(orderService).isInstanceOf(OrderServiceImpl.class);

  }


}
