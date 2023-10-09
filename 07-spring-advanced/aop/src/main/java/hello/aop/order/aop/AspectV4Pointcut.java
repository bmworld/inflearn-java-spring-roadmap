package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV4Pointcut {


  @Around("hello.aop.order.aop.Pointcuts.allOrder()") // <-- 위 @Pointcut 표현식을 사용할 수 있다.
  public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("[log] {}", joinPoint.getSignature());
    return joinPoint.proceed();
  }


  /**
   * Transaction 열고닫는 AOP (cf. Tx는 Log로 대체)
   */
  @Around("hello.aop.order.aop.Pointcuts.orderAndService()") // hello.aop.order Package & 하위 패키지이면서 동시에 ClassName Pattern이 *Service
  public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
    try{
      log.info("[Start Transaction] {}", joinPoint.getSignature());
      // =================================================================
      Object result = joinPoint.proceed();
      // =================================================================
      log.info("[Commit Transaction] {}", joinPoint.getSignature());
      return result;
    } catch(Exception e) {
        log.info("[Rollback Transaction] {}", joinPoint.getSignature());
        throw e;
    } finally {
      log.info("[Release Resource] {}", joinPoint.getSignature());
    }
  }
}
