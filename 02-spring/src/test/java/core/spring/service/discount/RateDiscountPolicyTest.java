package core.spring.service.discount;

import core.spring.domain.Grade;
import core.spring.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RateDiscountPolicyTest {

  RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

  @Test
  @DisplayName("VIP등급 10%할인")
  public void VIPMember_Discount10Percent() throws Exception{
    // Given
    Member member = new Member(1L, "VIP member", Grade.VIP);
    // When
    int price = 10000;
    int discount = discountPolicy.discount(member, price);
    // VID 10% 할인을 적용했으므로
    int result = 1000;

    // Then
    assertThat(discount).isEqualTo(result);

  }

  @Test
  @DisplayName("VIP가 아닌 경우, 할인 없음")
  public void basicMember_NoDiscount() throws Exception{
    // Given
    Member member = new Member(2L, "Basic member", Grade.BASIC);
    // When
    int price = 10000;
    int discount = discountPolicy.discount(member, price);
    int noDiscount = 0;


    // Then
    assertThat(discount).isEqualTo(noDiscount);

  }

}
