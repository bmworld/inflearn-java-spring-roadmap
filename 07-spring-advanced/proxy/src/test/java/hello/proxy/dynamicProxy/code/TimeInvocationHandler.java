package hello.proxy.dynamicProxy.code;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * <h1>동적 Proxy에 적용할 Handler Logic</h1>
 */
@Slf4j
public class TimeInvocationHandler implements InvocationHandler {

  private final Object target;

  public TimeInvocationHandler(Object target) {
    this.target = target;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    log.info("Execute TimeProxy !");
    long startTime = System.currentTimeMillis();

    // =================================================
    Object result = method.invoke(target, args); // Reflection 사용하여, `target` instance의 Method 실행 / `args`는 Method 호출 시, 넘겨줄 인수.
    // =================================================

    long endTime = System.currentTimeMillis();
    long resultTime = endTime - startTime;
    log.info("End TimeProxy resultTime={}", resultTime);
    return result;
  }
}
