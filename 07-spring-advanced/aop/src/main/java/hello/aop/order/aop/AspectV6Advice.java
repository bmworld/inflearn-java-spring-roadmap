package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;


@Slf4j
@Aspect
public class AspectV6Advice {

//  @Around("hello.aop.order.aop.Pointcuts.allOrder()") // <-- 위 @Pointcut 표현식을 사용할 수 있다.
  public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
    log.info("[log] {}", joinPoint.getSignature());
    return joinPoint.proceed();
  }


  @Around("hello.aop.order.aop.Pointcuts.orderAndService()") // hello.aop.order Package & 하위 패키지이면서 동시에 ClassName Pattern이 *Service
  public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
    try{
      // = @Before
      log.info("[@Around] [Start Transaction] {}", joinPoint.getSignature());
      // =================================================================
      Object result = joinPoint.proceed();
      // =================================================================
      // = @AfterReturning
      log.info("[@Around] [Commit Transaction] {}", joinPoint.getSignature());
      return result;
    } catch(Exception e) {
      // = @AfterThrowing
      log.info("[@Around] [Rollback Transaction] {}", joinPoint.getSignature());
      throw e;
    } finally {
      // = @After
      log.info("[@Around] [Release Resource] {}", joinPoint.getSignature());
    }
  }

  @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
  public void doBefore(JoinPoint joinPoint) {
    log.info("[@Before] {}", joinPoint.getSignature());
  }


  @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "returnVal")
  public void doAfterReturning(JoinPoint joinPoint, Object returnVal) {
    log.info("[@AfterReturning] {}, returning={}", joinPoint.getSignature(), returnVal);
  }

  @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
  public void doAfterThrowing(JoinPoint joinPoint, Exception ex) {
    log.info("[@AfterThrowing] {} ex={}",joinPoint.getSignature(), ex);
  }


  @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
  public void doAfter(JoinPoint joinPoint) {
    log.info("[@After] {} ex={}", joinPoint.getSignature());
  }
}
