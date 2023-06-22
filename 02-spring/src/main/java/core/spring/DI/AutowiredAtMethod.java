package core.spring.DI;

import core.spring.repository.MemberRepository;
import core.spring.service.MemberService;
import core.spring.service.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>의존성 주입: 일반 메서드Setter를 통한 @Autowired</h1>
 * <h2>[주의] 일반적으로 잘 사용하지 않는다.</h2>
 * <pre>
 *   - 장점: 한번에 여러 필드를 주입받을 수 있다.
 * </pre>
 */
@Component
public class AutowiredAtMethod {
  private MemberRepository memberRepository;


  @Autowired
  public void init(MemberRepository memberRepository) {
    System.out.println("----- 일반 메서드(Setter)에 @Autowired 붙이는 방법을 사용한 의존관계 주입 > memberRepository = " + memberRepository);
    this.memberRepository = memberRepository;
  }


}
