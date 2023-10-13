package hello.aop.proxyVersus;

import hello.aop.member.MemberService;
import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ProxyCastingTest {

  @Test
  @DisplayName("Type Casting: JDK Proxy (interface 타입캐스팅) -> 성공")
  void success_JdkDynamicProxy() {
    MemberServiceImpl target = new MemberServiceImpl();
    ProxyFactory proxyFactory = new ProxyFactory(target);
    proxyFactory.setProxyTargetClass(false); // JDK Dynamic Proxy 로 설정한다.


    // Success Type Casting from `Proxy class` to `interface`
    MemberService proxy = (MemberService) proxyFactory.getProxy();
    proxy.hello("hello");
  }

  /**
   * <h1>`JDK Dynamic Proxy`는 interface 타입 변환 가능 / 구체클래스 타입변환 불가능</h1>
   * <pre>
   * JDK Dynamic Proxy는 Interface를 기반으로 생성된다.
   * JDK Dynamic Proxy는 구체 클래스는 전혀 모른다.
   *  -> 따라서, 구체 클래스로 (또는 자식) Type Casting 불가
   * </pre>
   */
  @Test
  @DisplayName("Type Casting: JDK Proxy (Concrete Class) -> 실패")
  void failed_JdkDynamicProxy() {
    MemberServiceImpl target = new MemberServiceImpl();
    ProxyFactory proxyFactory = new ProxyFactory(target);
    proxyFactory.setProxyTargetClass(false); // JDK Dynamic Proxy 로 설정한다.


    // Failed Type Casting from `JDK Dynamic Proxy` to `Concrete Class`
    Assertions.assertThatThrownBy(() -> {
      MemberServiceImpl proxy = (MemberServiceImpl) proxyFactory.getProxy(); // 잘못된 타입캐스팅 시도 -> Exception 터짐
    }).isInstanceOf(ClassCastException.class);
  }


  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  // =================================================================================================
  @Test
  @DisplayName("Type Casting: CGLIB (interface 타입캐스팅) -> 성공")
  void success_CGLIB_by_Interface() {
    MemberServiceImpl target = new MemberServiceImpl();
    ProxyFactory proxyFactory = new ProxyFactory(target);
    proxyFactory.setProxyTargetClass(true); // CGLIB 라이브러리로 Proxy 생성함
    // Success Type Casting from `Proxy class` to `interface`
    MemberService proxy = (MemberService) proxyFactory.getProxy();
    proxy.hello("hello");
  }


  /**
   * <h1>`CGLIB`는 구체 클래스 기반으로 Proxy 생성 => `Interface`, `Concrete Class` 타입캐스팅 모두 가능</h1>
   * <pre>
   *   여기서 CGLIB Proxy는 `MemberServiceImpl` 구체 클래스 기반으로 생성된 Proxy이다.
   *   따라서, `MemberServiceImpl` 구체 클래스 및 `MemberServiceImpl`이 구현한 Interface인 `MemberService` 또한 타입 캐스팅 가능
   * </pre>
   */
  @Test
  @DisplayName("Type Casting: CGLIB (구현클래스 타입캐스팅) -> 성공")
  void success_CGLIB_by_ConcreteClass() {
    MemberServiceImpl target = new MemberServiceImpl();
    ProxyFactory proxyFactory = new ProxyFactory(target);
    proxyFactory.setProxyTargetClass(true); // CGLIB 라이브러리로 Proxy 생성함
    // Success Type Casting from `Proxy class` to `Concrete Class`
    MemberServiceImpl proxy = (MemberServiceImpl) proxyFactory.getProxy();
    proxy.hello("hello");
  }


}
