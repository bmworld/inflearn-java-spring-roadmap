package hello.proxy.advisor;

import hello.proxy.common.advice.CustomAdvice1;
import hello.proxy.common.advice.CustomAdvice2;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;


/**
 * <h1>[중요] Spring AOP 주요개념</h1>
 * <pre>
 *   * 하나의 `target`에 여러 AOP가 적용될 경우,
 *   Spring AOP는 `target`마다 `단 하나의 Proxy`만 생성하고, 여러 Advisor를 사용하는 방식으로 구현한다.
 *   => 이유: 최적화
 * </pre>
 */
@Slf4j
public class MultiAdvisorTest {
  @DisplayName("Use multiple Proxy: 복수의 프록시 & 복수의 어드바이저")
  @Test
  void multiAdvisorWithMultiProxy () {
    // FLOW
    // : client -> proxy2(advisor2) -> proxy1(advisor1) -> target


    // Proxy1 생성
    ServiceInterface target = new ServiceImpl();
    ProxyFactory proxyFactory1 = new ProxyFactory(target);
    DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new CustomAdvice1());
    proxyFactory1.addAdvisor(advisor1);
    ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();


    // Proxy2 생성 ( target -> proxy 1)
    ProxyFactory proxyFactory2 = new ProxyFactory(proxy1); // => Proxy Chain을 연결해주자.
    DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new CustomAdvice2());
    proxyFactory2.addAdvisor(advisor2);
    ServiceInterface proxy2 = (ServiceInterface) proxyFactory2.getProxy();

    proxy2.save();
    /**
     * RESULT
     * hello.proxy.common.advice.CustomAdvice2 - CustomAdvice2.invoke
     * hello.proxy.common.advice.CustomAdvice1 - CustomAdvice1.invoke
     * hello.proxy.common.service.ServiceImpl - Call Service save()
     */
  }


  /**
   *  복수의 Proxy CASE와 비교 시,
   *  결과 동일 & 더 나은 성능!
   */
  @DisplayName("Use multiple Proxy: 하나의 프록시 & 복수의 어드바이저")
  @Test
  void multiAdvisorByOnlyOneProxy () {
    // FLOW
    // : client -> proxy -> advisor2 -> advisor1 -> target
    DefaultPointcutAdvisor advisor1 = new DefaultPointcutAdvisor(Pointcut.TRUE, new CustomAdvice1());
    DefaultPointcutAdvisor advisor2 = new DefaultPointcutAdvisor(Pointcut.TRUE, new CustomAdvice2());


    // Proxy1 생성
    ServiceInterface target = new ServiceImpl();
    ProxyFactory proxyFactory1 = new ProxyFactory(target);

    // ================================================= Proxy는 넣은 순서대로 동작한다.
    proxyFactory1.addAdvisor(advisor2);
    proxyFactory1.addAdvisor(advisor1);
    // =================================================
    ServiceInterface proxy1 = (ServiceInterface) proxyFactory1.getProxy();

    proxy1.save();

    /**
     * RESULT (여러 Proxy 여러 Advisor
     * hello.proxy.common.advice.CustomAdvice2 - CustomAdvice2.invoke
     * hello.proxy.common.advice.CustomAdvice1 - CustomAdvice1.invoke
     * hello.proxy.common.service.ServiceImpl - Call Service save()
     */
  }
}
