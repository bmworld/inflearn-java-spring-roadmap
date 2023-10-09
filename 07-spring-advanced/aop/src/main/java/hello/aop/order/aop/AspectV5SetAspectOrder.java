package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * Aspect 실행순서는
 *  `@Aspect` Annotation 붙은 위치에 `@Order(..)`적용.
 *  (cf. `@Around` Annotation 붙은 위치에서는 적용 안 됨)
 *
 */

@Slf4j
@Aspect
public class AspectV5SetAspectOrder {

  @Aspect
  @Order(2)
  public static class LogAspect{
    @Around("hello.aop.order.aop.Pointcuts.allOrder()") // <-- 위 @Pointcut 표현식을 사용할 수 있다.
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
      log.info("[log] {}", joinPoint.getSignature());
      return joinPoint.proceed();
    }
  }


  @Aspect
  @Order(1)
  public static class TxAspect{
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


}
