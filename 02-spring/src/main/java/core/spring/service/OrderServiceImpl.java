package core.spring.service;

import core.spring.domain.Member;
import core.spring.domain.Order;
import core.spring.repository.MemberRepository;
import core.spring.service.discount.DiscountPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

  private final MemberRepository memberRepository;
  private final DiscountPolicy discountPolicy;

  @Override
  public Order createOrder(Long memberId, String itemName, int itemPrice) {
    Member member = memberRepository.findById(memberId);
    int discountPrice = discountPolicy.discount(member, itemPrice);

    return new Order(memberId, itemName, itemPrice, discountPrice);
  }

  // TEST 용
  public MemberRepository getMemberRepository() {
    return memberRepository;
  }
}
