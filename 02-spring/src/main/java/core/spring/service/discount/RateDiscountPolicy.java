package core.spring.service.discount;

import core.spring.annotation.MainDiscountPolicy;
import core.spring.domain.Grade;
import core.spring.domain.Member;
import org.springframework.stereotype.Component;

@Component
@MainDiscountPolicy
public class RateDiscountPolicy implements DiscountPolicy{
  @Override
  public int discount(Member member, int price) {
    return member.getGrade().equals(Grade.VIP) ? price * DiscountVar.RatedDiscountPercent / 100 : 0;
  }
}
