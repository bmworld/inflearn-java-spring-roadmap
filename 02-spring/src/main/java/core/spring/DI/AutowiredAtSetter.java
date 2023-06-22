package core.spring.DI;

import core.spring.repository.MemberRepository;
import core.spring.service.MemberService;
import core.spring.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>의존성 주입: 수정자 메서드Setter를 통한 @Autowired</h1>
 */
@Component
public class AutowiredAtSetter {
  private MemberRepository memberRepository;
  private MemberService memberService;

  private OrderServiceImpl orderService;

  @Autowired
  public void setMemberRepository(MemberRepository memberRepository) {
    System.out.println("----- 수정자 메서드(Setter)에 @Autowired 붙이는 방법을 사용한 의존관계 주입 > memberRepository = " + memberRepository);
    this.memberRepository = memberRepository;
  }

  public void setMemberService(MemberService memberService) {
    System.out.println("----- 수정자 메서드(Setter)에 @Autowired 없을 경우, 작동 안 함 > memberService = " + memberService);
    this.memberService = memberService;
  }


  @Autowired(required = false)
  public void setOrderService(OrderServiceImpl orderService) {
    System.out.println("-----  선택적 주입 @Autowired(required = false) > orderService = " + orderService);
    this.orderService = orderService;
  }

}
