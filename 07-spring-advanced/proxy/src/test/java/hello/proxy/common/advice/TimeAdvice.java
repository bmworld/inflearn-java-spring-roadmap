package hello.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/**
 * Target 없어도됨
 * Why?  ProxyFactory 생성시점에 Target을 넣어준다.
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    log.info("[@Override] Operate TimeProxy");
    long startTime = System.currentTimeMillis();

    // =================================================================
    Object result = invocation.proceed();// proceed 사용 시, Proxy의 Target을 찾아서, Args 넘겨서 Target을 실행해줌.
// =================================================================
    long endTime = System.currentTimeMillis();
    long workingTime = endTime - startTime;
    log.info("TimeDecorator 종료 > workingTime = {}ms", workingTime);
    return result;
  }
}
