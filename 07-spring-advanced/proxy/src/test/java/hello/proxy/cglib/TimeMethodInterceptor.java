package hello.proxy.cglib;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * <h1>CGLIB를 굳이. 직접 사용해보자. (일반적으로 직접 사용할 일은 드물다.)</h1>
 */
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {
  private final Object target;

  public TimeMethodInterceptor(Object target) {
    this.target = target;
  }

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
    log.info("Execute TimeProxy !");
    long startTime = System.currentTimeMillis();

    // =================================================
    // cglib 에서는 methodProxy 사용 시, method 보다 성능이 더 빠르다고 함.
    Object result = methodProxy.invoke(target, args); // Reflection 사용하여, `target` instance의 Method 실행 / `args`는 Method 호출 시, 넘겨줄 인수.
    // =================================================

    long endTime = System.currentTimeMillis();
    long resultTime = endTime - startTime;
    log.info("End TimeProxy by `CGLIB` resultTime={}", resultTime);
    return result;
  }
}
