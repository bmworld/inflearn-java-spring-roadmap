package hello.aop.internalCall.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class CallAspect {
  @Before("execution(* hello.aop.internalCall..*.*(..))")
  public void doLog(JoinPoint joinPoint) {
    log.info("[AOP] {}", joinPoint.getSignature());

  }
}
