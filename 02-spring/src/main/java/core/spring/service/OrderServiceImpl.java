package core.spring.service;

import core.spring.domain.Member;
import core.spring.domain.Order;
import core.spring.repository.MemberRepository;
import core.spring.repository.MemoryMemberRepository;
import core.spring.service.discount.DiscountPolicy;
import core.spring.service.discount.RateDiscountPolicy;

public class OrderServiceImpl implements OrderService{

  private final MemberRepository memberRepository;
  private final DiscountPolicy discountPolicy;

  public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
  }
//  private DiscountPolicy discountPolicy;

  @Override
  public Order createOrder(Long memberId, String itemName, int itemPrice) {
    Member member = memberRepository.findById(memberId);
    int discountPrice = discountPolicy.discount(member, itemPrice);

    return new Order(memberId, itemName, itemPrice, discountPrice);
  }
}
