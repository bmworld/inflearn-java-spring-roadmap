package hello.proxy.config.v6_aop.aspect;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;


/**
 * <h1>@Aspect</h1>
 * <pre>
 *   - Annotation 을 통하여, `Advice` & `pointcut`을 편리하게 생성할 수 있다.
 * </pre>
 */
@Slf4j
@Aspect
public class LogTraceAspect {
  private final LogTrace logTrace;

  public LogTraceAspect(LogTrace logTrace) {
    this.logTrace = logTrace;
  }




  // ===================================== pointCut 부분 ================================
  @Around("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))")
  // ===================================== pointCut 부분 ================================
  public Object execute(ProceedingJoinPoint joinPoint) throws Throwable{

    // ==================================================================================
    // =================================== Advice 부분  ==================================
    // ==================================================================================
    TraceStatus status = null;
    try{
      String message = getMessage(joinPoint); // 요걸로 꺼내시라.
      status = logTrace.begin(message);
      // =================================================================
      // Logic 호출
      Object result = joinPoint.proceed();//****** 이게 진짜 편리.
      // =================================================================

      logTrace.end(status);
      return result;
    } catch(Exception e) {
      logTrace.exception(status, e);
      throw e; // 원래 Exception 은 그.대.로. 적용되게 만든다.
    }


    // ==================================================================================
    // =================================== Advice 부분  ==================================
    // ==================================================================================
  }

  private String getMessage(ProceedingJoinPoint joinPoint) {
    return joinPoint.getSignature().toShortString() + "() by `@Aspect > ProceedingJoinPoint`";
  }
}
