package hello.aop.pointcut;

import hello.aop.member.annotation.ClassAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;


/**
 * <h1>[주의사항] pointcut 지시자 - 단독 사용 조건</h1>
 * <pre>
 *   `args`, `@args`, `@target`
 *   : 위 3가지 pointcut 지시자는 Proxy가 존재해야 Advice 적용여부를 판단할 수 있다.
 *   - 위 3가지 pointcut 지시자가 존재하면, Spring 은 모든 Spring Bean에 AOP를 적용하려고 시도한다.
 *   그러나, proxy 생성 이전 시점에 판단 자체가 불가하므로, Ex 발생한다.
 *   ( cf. Proxy 생성시점: spring container 만들어지는 App Loading 시점 )
 *
 *
 *   ** 따라서, 위 3가지 표현식은 최대한 [Proxy 적용 대상을 축소하는 표현식]과 함께 사용해야 한다.
 *
 *
 *
 * </pre>
 */
@Slf4j
@Import({AtTargetAndAtWithinTest.Config.class})
@SpringBootTest
public class AtTargetAndAtWithinTest {
  @Autowired
  private Child child;

  @Test
  void success() {
    log.info("child Proxy={}", child.getClass());
    child.childMethod(); //부모, 자식 모두 있는 메서드
    child.parentMethod(); //부모 클래스만 있는 메서드
  }

  static class Config {

    @Bean
    public Parent parent() {
      return new Parent();
    }
    @Bean
    public Child child() {
      return new Child();
    }
    @Bean
    public AtTargetAtWithinAspect atTargetAtWithinAspect() {
      return new AtTargetAtWithinAspect();
    }
  }

  static class Parent {
    public void parentMethod(){} //부모에만 있는 메서드
  }

  @ClassAop
  static class Child extends Parent {
    public void childMethod(){}
  }

  @Slf4j
  @Aspect
  static class AtTargetAtWithinAspect {

    // `@target`: 인스턴스 기준으로 모든 메서드의 조인 포인트를 선정, 부모 타입의 메서드도 적용
    @Around("execution(* hello.aop..*(..)) && @target(hello.aop.member.annotation.ClassAop)")
    public Object atTarget(ProceedingJoinPoint joinPoint) throws Throwable {
      log.info("[@target] {}", joinPoint.getSignature());
      return joinPoint.proceed();
    }

    // `@within`: 선택된 클래스 내부에 있는 메서드만 조인 포인트로 선정, 부모 타입의 메서드는 적용되지 않음
    @Around("execution(* hello.aop..*(..)) && @within(hello.aop.member.annotation.ClassAop)")
    public Object atWithin(ProceedingJoinPoint joinPoint) throws Throwable {
      log.info("[@within] {}", joinPoint.getSignature());
      return joinPoint.proceed();
    }
  }
}
