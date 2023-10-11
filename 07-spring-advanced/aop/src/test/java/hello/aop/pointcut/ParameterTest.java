package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * <h1>Pointcut > Parameter 정보 가져오기</h1>
 */
@Slf4j
@SpringBootTest
@Import(ParameterTest.ParameterAspect.class)
public class ParameterTest {
  @Autowired
  MemberService memberService;

  @Test
  void success() {
    log.info("memberService Proxy={}", memberService.getClass());
    memberService.hello("helloA");
  }

  @Aspect
  static class ParameterAspect {

    @Pointcut("execution(* hello.aop.member..*.*(..))")
    private void allMember() {
    }

    @Around("allMember()")
    public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
      Object arg1 = joinPoint.getArgs()[0];
      log.info("[logArgs1] {}, arg={}", joinPoint.getSignature(), arg1);
      return joinPoint.proceed();
    }

    @Around("allMember() && args(arg,..)")
    public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
      log.info("[logArgs2] {}, arg={}", joinPoint.getSignature(), arg);
      return joinPoint.proceed();
    }

    @Before("allMember() && args(arg,..)")
    public void logArgs3(String arg) {
      log.info("[logArgs3] arg={}", arg);
    }


    /**
     * `this`: Spring Bean 객체가 대상인 JoinPoint (여기서는 Spring AOP Proxy)
     * ex) obj=class hello.aop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$8fab05ab
     */
    @Before("allMember() && this(obj)")
    public void thisArgs(JoinPoint joinPoint, MemberService obj) {
      log.info("[this] {}, obj={}", joinPoint.getSignature(), obj.getClass());
    }

    /**
     * `target`: 실제 대상 구현체가 대상인 JoinPoint (여기서는 Spring AOP Proxy가 가르키는 실제 대상)
     * ex) obj=class hello.aop.member.MemberServiceImpl
     */
    @Before("allMember() && target(obj)")
    public void targetArgs(JoinPoint joinPoint, MemberService obj) {
      log.info("[target] {}, obj={}", joinPoint.getSignature(), obj.getClass());
    }

    /**
     *  `@target` 해당 Type의 annotation을 전달받는다.
     */
    @Before("allMember() && @target(annotation)")
    public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
      log.info("[@target > @ClassAop] {}, obj={}", joinPoint.getSignature(), annotation);
    }


    /**
     *  `@within` 해당 Type의 annotation을 전달받는다. ( `@target`과 유사)
     */
    @Before("allMember() && @within(annotation)")
    public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
      log.info("[@within] {}, obj={}", joinPoint.getSignature(), annotation);
    }


    /**
     * `@annotation`: `Method`의 annotation 전달받는다.
     *              + 해당 Method의 `Value`를 가져올 수 있다.
     */
    @Before("allMember() && @annotation(annotation)")
    public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
      log.info("[@annotation]{}, annotation={}, annotationValue={}", joinPoint.getSignature(), annotation, annotation.value());
    }


  }
}
