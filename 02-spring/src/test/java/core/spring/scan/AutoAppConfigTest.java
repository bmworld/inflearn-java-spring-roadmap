package core.spring.scan;

import core.spring.config.AutoAppConfig;
import core.spring.repository.MemoryMemberRepository;
import core.spring.service.MemberService;
import core.spring.service.OrderService;
import core.spring.service.OrderServiceImpl;
import core.spring.service.discount.RateDiscountPolicy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


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
    Assertions.assertThat(memberService).isInstanceOf(MemberService.class);

    RateDiscountPolicy rateDiscountPolicy = ac.getBean(RateDiscountPolicy.class);
    Assertions.assertThat(rateDiscountPolicy).isInstanceOf(RateDiscountPolicy.class);

    MemoryMemberRepository memoryMemberRepository = ac.getBean(MemoryMemberRepository.class);
    Assertions.assertThat(memoryMemberRepository).isInstanceOf(MemoryMemberRepository.class);

    OrderService orderService = ac.getBean(OrderService.class);
    Assertions.assertThat(orderService).isInstanceOf(OrderServiceImpl.class);

  }
}
