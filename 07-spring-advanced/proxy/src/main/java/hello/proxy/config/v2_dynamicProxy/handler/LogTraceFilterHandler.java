package hello.proxy.config.v2_dynamicProxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHandler implements InvocationHandler {

  private final Object target;
  private final LogTrace logTrace;

  private final String[] patterns;

  public LogTraceFilterHandler(Object target, LogTrace logTrace, String[] patterns) {
    this.target = target;
    this.logTrace = logTrace;
    this.patterns = patterns;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    // Filter : Proxy 적용대상 method 외에는 제외시켜보자.
    String methodName = method.getName();
    if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
      return method.invoke(target, args); // 패턴 불일치 시, 원래 로직만 실행되도록.
    }


    TraceStatus status = null;
    try{
      status = logTrace.begin(getMessage(method));
      // =================================================================
      // Logic 호출
      Object result = method.invoke(target, args);
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
