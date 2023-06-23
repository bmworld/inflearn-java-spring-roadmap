package core.spring.service;

import core.spring.domain.Member;
import core.spring.domain.Order;
import core.spring.repository.MemberRepository;
import core.spring.service.discount.DiscountPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class OrderServiceImpl implements OrderService{

  private final MemberRepository memberRepository;
  private final DiscountPolicy discountPolicy;


  /**
   *  @Qualfier: Spring bean 이름이 겹칠 경우, 각 class에 @Qualifier를 부여한다.
   * 해당 bean을 사용할 곳에서, 선한한 여러 qualifier 중에서, 하나를 고르면 된다.
   * ( 중요: 선언된 @Qualfier와 사용할 대상의  @Qualfier 이름이 동일해야한ㄷ.
   */
  @Autowired
  public OrderServiceImpl(MemberRepository memberRepository, @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy) {
    this.memberRepository = memberRepository;
    this.discountPolicy = discountPolicy;
  }
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
