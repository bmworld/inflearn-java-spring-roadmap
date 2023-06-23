package core.spring.service.discount;

import core.spring.domain.Grade;
import core.spring.domain.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscountPolicy{

  private int discountFixAmount = FixedDiscountVar.discountAmount;
  @Override
  public int discount(Member member, int price) {
    return member.getGrade() == Grade.VIP ? discountFixAmount : 0;

  }
}
