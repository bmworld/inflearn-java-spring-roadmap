package core.spring.service;

import core.spring.domain.Member;
import core.spring.domain.Order;
import core.spring.repository.MemberRepository;
import core.spring.repository.MemoryMemberRepository;
import core.spring.service.discount.DiscountPolicy;
import core.spring.service.discount.FixDiscountPolicy;

public class OrderServiceImpl implements OrderService{

  private final MemberRepository memberRepository = new MemoryMemberRepository();
  private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

  @Override
  public Order createOrder(Long memberId, String itemName, int itemPrice) {
    Member member = memberRepository.findById(memberId);
    int discountPrice = discountPolicy.discount(member, itemPrice);

    return new Order(memberId, itemName, itemPrice, discountPrice);
  }
}
