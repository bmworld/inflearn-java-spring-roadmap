package hello.aop.proxyVersus;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import hello.aop.proxyVersus.code.ProxyDIAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * <h1>핵심</h1>
 * <pre>
 *   - JDK Dynamic Proxy: 주입 대상 객체인 `MemberServiceImpl` 타입에 의존관계 주입 불가
 *   - CGLIB Proxy: 주입 대상 객체인 `MemberServiceImpl` 타입에 의존관계 주입 가능
 * </pre>
 */
@Slf4j
// 참조)  properties 옵션 사용 시, 해당 TEST 에서만 설정을 임시로 적용할 수 있음.
//@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"}) // JDK Dynamic Proxy 설정.
@SpringBootTest(properties = {"spring.aop.proxy-target-class=true"}) // CGLIB 설정
@Import(ProxyDIAspect.class)
public class ProxyDITest {

  @Autowired
  private MemberService memberService;

  /**
   *
   * <h1>JDK Dynamic Proxy 설정 사용 시, Exception 발생</h1>
   * <pre>
   *   @Config `@SpringBootTest(properties = {"spring.aop.proxy-target-class=false"})`
   *   @ErrorMesasage: Unsatisfied dependency expressed through field 'memberServiceImpl'; nested exception is org.springframework.beans.factory.BeanNotOfRequiredTypeException: Bean named 'memberServiceImpl' is expected to be of type 'hello.aop.member.MemberServiceImpl' but was actually of type 'com.sun.proxy.$Proxy58'
   * </pre>
   */
  @Autowired
  private MemberServiceImpl memberServiceImpl;

  @Test
  void go () {
    log.info("memberService class={}", memberService.getClass());
    log.info("memberServiceImpl class={}", memberServiceImpl.getClass());
  }
}
