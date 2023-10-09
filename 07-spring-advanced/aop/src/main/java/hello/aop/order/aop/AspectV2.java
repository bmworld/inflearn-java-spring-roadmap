package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

  // hello.aop.order 패키지와 하위 패키지를 모두 가져옴
  @Pointcut("execution(* hello.aop.order..*(..))")
  private void allOrder() {

  } // pointcut signature -> 여러 AOP에서 사용할 수 있다


  @Around("allOrder()") // <-- 위 @Pointcut 표현식을 사용할 수 있다.
  public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("[log] {}", joinPoint.getSignature());
    return joinPoint.proceed();
  }
}
