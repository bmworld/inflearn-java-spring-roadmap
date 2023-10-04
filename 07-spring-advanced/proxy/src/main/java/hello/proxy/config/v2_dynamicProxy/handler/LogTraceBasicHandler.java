package hello.proxy.config.v2_dynamicProxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceBasicHandler implements InvocationHandler {

  private final Object target;
  private final LogTrace logTrace;

  public LogTraceBasicHandler(Object target, LogTrace logTrace) {
    this.target = target;
    this.logTrace = logTrace;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

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
