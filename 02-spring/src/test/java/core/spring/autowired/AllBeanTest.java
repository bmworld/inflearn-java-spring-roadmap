package core.spring.autowired;

import core.spring.config.AutoAppConfig;
import core.spring.domain.Grade;
import core.spring.domain.Member;
import core.spring.service.discount.DiscountPolicy;
import core.spring.service.discount.DiscountVar;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AllBeanTest {
  @Test
  @DisplayName("모든 Bean 조회 (Map방식, List 방식 ...)")
  public void scanAllTargetBean() throws Exception {
    // Given
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DiscountService.class);


    DiscountService discountService = ac.getBean(DiscountService.class);
    Member member = new Member(1L, "userA", Grade.VIP);
    int price = 123124;
    int fixedDiscountPrice = discountService.discount(member, price, "fixDiscountPolicy");
    int RateDiscountPrice = discountService.discount(member, price, "rateDiscountPolicy");

    assertThat(discountService).isInstanceOf(DiscountService.class);
    assertThat(fixedDiscountPrice).isEqualTo(DiscountVar.fixedDiscountAmount);
    assertThat(RateDiscountPrice).isEqualTo(price/DiscountVar.RatedDiscountPercent);

  }

  public static class DiscountService {
    public final Map<String, DiscountPolicy> policyMap;
    private final List<DiscountPolicy> policies;

    @Autowired  // Contructor가 하나인 경우, @Autowired 생략가능 -> Spring이 알아서 Bean 주입함.
    public DiscountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
      this.policyMap = policyMap;
      this.policies = policies;
//      policyMap.forEach((string, index) -> {
//        System.out.println("---- string = " + string  + " /  index  = " +  index);
//      });
    }

    public int discount(Member member, int price, String discountCode) {
      DiscountPolicy discountPolicy = policyMap.get(discountCode);
      return discountPolicy.discount(member, price);
    }
  }
}
