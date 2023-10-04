package hello.proxy.dynamicProxy;

import hello.proxy.dynamicProxy.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

/**
 * <h1>Proxy 생성 방법</h1>
 * <pre>
 *   `JDK 동적 Proxy`를 사용하여, 동적으로 만든다. (직접 Proxy를 만들지 않아도 된다)
 *    newProxyInstance 내 아래 3가지를 넣어준다.
 *   1. class Loader()
 *   2. interface
 *   3. Handler Logic
 *
 *   - 런타임 의존관계
 *     : client -> $proxy1 -> handler invoke() -> targetInterface
 * </pre>
 */
@Slf4j
public class JdkDynamicProxyTest {
  @Test
  void dynamicProxyA() {
    AInterface target = new AImpl();
    TimeInvocationHandler handler = new TimeInvocationHandler(target);
    AInterface proxy = (AInterface) Proxy.newProxyInstance(AInterface.class.getClassLoader(), new Class[]{AInterface.class}, handler);// => 인수: (인터페이스의 로더, 해당 클래스가 구현하는 인터페이스 배열, 프록시가 처리할 로직)
    proxy.call(); // proxy의 메서드 실행 시, proxy 생성 시에 주입된 handler (InvocationHandler) 실행 ( *정확히는 해당 클래스 내부ㅠ invoke() 실행)

    log.info("targetClass={}", target.getClass());
    log.info("proxyClass={}", proxy.getClass()); // ex) proxyClass=class com.sun.proxy.$Proxy8

  }


  @Test
  void dynamicProxy() {
    BInterface target = new BImpl();
    TimeInvocationHandler handler = new TimeInvocationHandler(target);
    BInterface proxy = (BInterface) Proxy.newProxyInstance(BInterface.class.getClassLoader(), new Class[]{BInterface.class}, handler);// => 인수: (인터페이스의 로더, 해당 클래스가 구현하는 인터페이스 배열, 프록시가 처리할 로직)
    proxy.call(); // proxy의 메서드 실행 시, proxy 생성 시에 주입된 handler (InvocationHandler) 실행 ( *정확히는 해당 클래스 내부의 invoke() 실행)

    log.info("targetClass={}", target.getClass());
    log.info("proxyClass={}", proxy.getClass()); // ex) proxyClass=class com.sun.proxy.$Proxy9

  }
}
