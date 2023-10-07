package hello.proxy.config.v3_proxyFactory.advice;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

/**
 * Spring 제공 Advice를 사용하여 proxy를 적용해보자.
 */
@Slf4j
public class LogTraceAdvice implements MethodInterceptor {

  public LogTraceAdvice(LogTrace logTrace) {
    this.logTrace = logTrace;
  }

  private final LogTrace logTrace;

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    TraceStatus status = null;
    try{
      Method method = invocation.getMethod();
      status = logTrace.begin(getMessage(method));
      // =================================================================
      // Logic 호출
      Object result = invocation.proceed();//****** 이게 진짜 편리.
// =================================================================

      logTrace.end(status);
      return result;
    } catch(Exception e) {
      logTrace.exception(status, e);
      throw e; // 원래 Exception 은 그.대.로. 적용되게 만든다.
    }

  }

  private String getMessage(Method method) {
    return method.getDeclaringClass().getSimpleName() + "." + method.getName() + "() by `Dynamic Proxy`";
  }
}
