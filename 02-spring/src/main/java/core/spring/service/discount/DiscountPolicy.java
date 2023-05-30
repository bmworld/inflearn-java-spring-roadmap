package core.spring.service.discount;

import core.spring.domain.Member;

public interface DiscountPolicy {

  /**
   * @return 할인 대상금액
   */
  int discount(Member member, int price);

}
