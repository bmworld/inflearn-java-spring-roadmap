package core.spring.DI;

import core.spring.repository.MemberRepository;
import core.spring.service.MemberService;
import org.springframework.stereotype.Component;


/**
 * <h1>Constructor 하나만 사용 시, "@Autowired 없이"도, 자동 생성자 주입.</h1>
 */
@Component
public class HiddenAutowiredWhenOnlyOneConstructor {
  private final MemberRepository memberRepository;
  private final MemberService memberService;

  public HiddenAutowiredWhenOnlyOneConstructor(MemberRepository memberRepository, MemberService memberService) {
    System.out.println("----- Constructor 하나만 사용 시, @Autowired 없이 자동 생성자 주입 > memberRepository = " + memberRepository + " / memberService = " + memberService);
    this.memberRepository = memberRepository;
    this.memberService = memberService;
  }


}
