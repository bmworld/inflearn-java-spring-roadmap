package hello.aop.pointcut;


import hello.aop.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * <h1>This & Target 지정 시, 유의사항</h1>
 * <pre>
 *   1) JDK Dynamic Proxy 방식: `this`에 `Concrete Class`를 지정 시, ** Advice 적용 불가 **
 *   2) CGLIB Proxy 방식: `Interface` 및 `Concrete Class` 모두 Advice 적용 가능
 *
 * </pre>
 * <h2>Proxy 생성방법 spring config</h2>
 * <pre>
 *  application.properties
 *  spring.aop.proxy-target-class=true  CGLIB
 *  spring.aop.proxy-target-class=false JDK 동적 프록시
 * </pre>
 */
@Slf4j
@Import(ThisAndTargetTest.ThisTargetAspect.class)
//@SpringBootTest(properties = "spring.aop.proxy-target-class=false") // *** JDK 동적 프록시
@SpringBootTest(properties = "spring.aop.proxy-target-class=true") // *** CGLIB (cf. spring default config)
public class ThisAndTargetTest {
  @Autowired
  private MemberService memberService;

  @Test
  void success() {
    log.info("memberService Proxy={}", memberService.getClass());
    memberService.hello("helloA");
  }

  @Aspect
  static class ThisTargetAspect {

    //부모 타입 허용
    @Around("this(hello.aop.member.MemberService)")
    public Object doThisInterface(ProceedingJoinPoint joinPoint) throws Throwable {
      log.info("[this-interface] {}", joinPoint.getSignature());
      return joinPoint.proceed();
      // JDK Proxy: hello.aop.member.MemberService.hello(String)
      // CGLIB: hello.aop.member.MemberServiceImpl.hello(String)
    }

    //부모 타입 허용
    @Around("target(hello.aop.member.MemberService)")
    public Object doTargetInterface(ProceedingJoinPoint joinPoint) throws Throwable {
      log.info("[target-interface] {}", joinPoint.getSignature());
      return joinPoint.proceed();
      // JDK Proxy: hello.aop.member.MemberService.hello(String)
      // CGLIB: hello.aop.member.MemberServiceImpl.hello(String)
    }

    // 구체  Class
    @Around("this(hello.aop.member.MemberServiceImpl)")
    public Object doThis(ProceedingJoinPoint joinPoint) throws Throwable {
      log.info("[this-impl] {}", joinPoint.getSignature());
      return joinPoint.proceed();
      // JDK Proxy: ----- ?????? -----
      // CGLIB Proxy: hello.aop.member.MemberServiceImpl.hello(String)
    }

    // 구체 Class
    @Around("target(hello.aop.member.MemberServiceImpl)")
    public Object doTarget(ProceedingJoinPoint joinPoint) throws Throwable {
      log.info("[target-impl] {}", joinPoint.getSignature()); //
      return joinPoint.proceed();
      // JDK Proxy: hello.aop.member.MemberService.hello(String)
      // CGLIB Proxy: hello.aop.member.MemberServiceImpl.hello(String)
    }
  }
}
