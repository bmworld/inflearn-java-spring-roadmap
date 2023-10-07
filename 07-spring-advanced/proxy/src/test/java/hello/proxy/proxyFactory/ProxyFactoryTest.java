package hello.proxy.proxyFactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class ProxyFactoryTest {

  @DisplayName("ProxyFactory: `interface` 존재 시 `JDK 동적 Proxy` 사용")
  @Test
  void interfaceProxy () {
    ServiceImpl target = new ServiceImpl();
    ProxyFactory proxyFactory = new ProxyFactory(target);
    proxyFactory.addAdvice(new TimeAdvice()); // Advice => Proxy 적용 대상 객체추가
    ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
    log.info("target Class ={}",target.getClass()); // class hello.proxy.common.service.ServiceImpl
    log.info("proxy Class ={}",proxy.getClass()); // class com.sun.proxy.$Proxy9

    proxy.save(); // ProxyFactory로 만든 Proxy로 원 메서드를 사용해보자.

    // 검증
    assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // ProxyFactory를 사용하여서 Proxy 생성했을 경우, 사용가능
    assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue(); // ----- JDKDynamicProxy = Interface 기반의 Proxy 생성한 경우.
    assertThat(AopUtils.isCglibProxy(proxy)).isFalse(); // ----- CGLIB Proxy = Clas 기반으로 Proxy 생성한 경우.
  }




  @DisplayName("ProxyFactory: `구체 클래스` 사용 시 `CGLIB Proxy` 사용")
  @Test
  void concreteProxy () {
    ConcreteService target = new ConcreteService();
    ProxyFactory proxyFactory = new ProxyFactory(target);
    proxyFactory.addAdvice(new TimeAdvice()); // Advice => Proxy 적용 대상 객체추가
    ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();
    log.info("target Class ={}",target.getClass()); // class hello.proxy.common.service.ConcreteService
    log.info("proxy Class ={}",proxy.getClass()); // class hello.proxy.common.service.ConcreteService$$EnhancerBySpringCGLIB$$29c0d4c7

    proxy.call(); // ProxyFactory로 만든 Proxy로 원 메서드를 사용해보자.

    // 검증
    assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // ProxyFactory를 사용하여서 Proxy 생성했을 경우, 사용가능
    assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse(); // ----- JDKDynamicProxy = Interface 기반의 Proxy 생성한 경우.
    assertThat(AopUtils.isCglibProxy(proxy)).isTrue(); // ----- CGLIB Proxy = Clas 기반으로 Proxy 생성한 경우.
  }





  @DisplayName("ProxyFactory: ProxyTargetClass Option 사용 시, Interface가 존재하더라도, CGLIB 사용하여, Class 기반 Proxy 사용함.")
  @Test
  void proxyTargetClass () {
    ServiceImpl target = new ServiceImpl();
    ProxyFactory proxyFactory = new ProxyFactory(target);
    // =================================
    proxyFactory.setProxyTargetClass(true); // Option: 항상 CGLIB 기반 Proxy 생성
    // =================================
    proxyFactory.addAdvice(new TimeAdvice()); // Advice => Proxy 적용 대상 객체추가
    ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
    log.info("target Class ={}",target.getClass()); // class hello.proxy.common.service.ServiceImpl
    log.info("proxy Class ={}",proxy.getClass()); // class hello.proxy.common.service.ServiceImpl$$EnhancerBySpringCGLIB$$bb4418a (** interface를 상속받아서, 구체클래스로 만듦)

    proxy.save(); // ProxyFactory로 만든 Proxy로 원 메서드를 사용해보자.

    // 검증
    assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // ProxyFactory를 사용하여서 Proxy 생성했을 경우, 사용가능
    assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse(); // ----- JDKDynamicProxy = Interface 기반의 Proxy 생성한 경우.
    assertThat(AopUtils.isCglibProxy(proxy)).isTrue(); // ----- CGLIB Proxy = Clas 기반으로 Proxy 생성한 경우.
  }
}
