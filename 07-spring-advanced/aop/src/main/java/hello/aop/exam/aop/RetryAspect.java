package hello.aop.exam.aop;

import hello.aop.exam.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class RetryAspect {

  @Around("@annotation(retry)")
  public Object retry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
    log.info("[retry] {} @retry={}", joinPoint.getSignature(), retry);

    int maxRetryCount = retry.value();
    Exception exceptionHolder = null;
    // 재시도 로직 시작.
    for (int retryCount = 1; retryCount <= maxRetryCount; retryCount++) {
      try{
        log.info("[retry] tryCount ={}/{}", retryCount, maxRetryCount);
        return joinPoint.proceed();
      } catch(Exception e) {
        exceptionHolder = e;
      }
    }


    // 위에서 retry count 만큼 시도했는데도 proceed 되지 않았다면, Exception 터뜨린다.
    throw exceptionHolder;

  }
}
