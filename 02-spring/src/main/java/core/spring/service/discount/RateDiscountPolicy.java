package core.spring.service.discount;

import core.spring.domain.Grade;
import core.spring.domain.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("mainDiscountPolicy")
public class RateDiscountPolicy implements DiscountPolicy{
  private int discountPercent = 10;
  @Override
  public int discount(Member member, int price) {
    return member.getGrade().equals(Grade.VIP) ? price * discountPercent / 100 : 0;
  }
}
