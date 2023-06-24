package core.spring.service.discount;

import core.spring.domain.Grade;
import core.spring.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class FixDiscountPolicy implements DiscountPolicy{

  private int discountFixAmount = DiscountVar.fixedDiscountAmount;
  @Override
  public int discount(Member member, int price) {
    return member.getGrade() == Grade.VIP ? discountFixAmount : 0;

  }
}
