package hello.proxy.cglib;

import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

/**
 * <h1>CGLIB 만들기</h1>
 * <pre>
 *   `Enhancer`: CGLIB를 사용한 proxy 생성에 사용됨
 * </pre>
 * <h1>`JDK 동적 Proxy` vs `CGLIB`</h1>
 * <pre>
 *   - JDK 동적 Proxy: Interface`구현`을 통해 Proxy 생성
 *     => ex) proxyClass = class `대상클래스.$Proxy1`
 *   - CGLIB: 구체 Class를 상속(extends)하여 Proxy 생성
 *     => ex) proxyClass = class `대상클래스$$EnhancerByCGLIB$$임의코드`
 * </pre>
 */
@Slf4j
public class CglibTest {
  @Test
  void cglib() {

    ConcreteService target = new ConcreteService();
    Enhancer enhancer = new Enhancer();// CGLIB를 만들자.
    enhancer.setSuperclass(ConcreteService.class); // CGLIB는 구체 Class를 상속받아서 Proxy 생성하며, 어떤 구체클래스를 상속받을지 지정함.
    enhancer.setCallback(new TimeMethodInterceptor(target)); // Proxy에 적용할 실행 로직 할당

    ConcreteService proxy = (ConcreteService) enhancer.create();// proxy 생성
    log.info("target class = " + target.getClass()); // class hello.proxy.common.service.ConcreteService
    log.info("proxy class = " + proxy.getClass()); // class hello.proxy.common.service.ConcreteService$$EnhancerByCGLIB$$25d6b0e3

    //================================================================
    proxy.call();
    /**
     * hello.proxy.cglib.TimeMethodInterceptor - Execute TimeProxy !
     * hello.proxy.common.service.ConcreteService - Call ConcreteService
     * hello.proxy.cglib.TimeMethodInterceptor - End TimeProxy by `CGLIB` resultTime=20
     */
  }
}
