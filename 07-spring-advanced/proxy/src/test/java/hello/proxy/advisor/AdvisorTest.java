package hello.proxy.advisor;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Method;

/**
 * <h1>Spring AOP와 주요개념</h1>
 * <pre>
 * - `PointCut` (= `어디에?`)
 *   : 부가기능을 적용할 대상의 유무에 대하여 판단하는 Filtering Logic
 *   (쉽게 이해하기=> 어떤 Point에 기능을 적용할지 말지 잘라서(Cut) 구분)
 *   주로 클래스와 메서드 이름으로 할당함.
 *
 * - `Advice` ( = `조언을`)
 *   : 어떤 로직을 적용할 지 선택 (Proxy가 호출하는 부가 기능)
 *   (= Proxy의 Logic)
 *
 *
 * - `Advisor` ( = `어디에 + 어떤 조언을`할 것인가)
 *   : `PointCut` 하나와 `Advice` 하나를 가지고 있는 녀석.
 * </pre>
 */
@Slf4j
public class AdvisorTest {


  /**
   * <pre>
   *   - `DefaultPointcutAdvisor`: `Advisor` Interface의 가장 일반적인 구현체
   *     생성자를 통해 하나의 PointCut, 하나의 Advice를 넣어준다.
   *   - `PointCut.TURE`: 항상 `true`를 반환하는 `PointCut`
   *   - `new TimeAdvice` 앞서 개발한 `TimeAdvice`를 제공해주자.
   *   - `proxyFactory.addAdvisor(advisor)`: ProxyFactory에 적용할 advisor를 지정한다.
   *      ->Advisor는 내부에 PointCut & Advice 모두 가지고 있다
   *      -> 따라서 어디에 어떤 부가 기능을 ㅈ거용해야 할지 어드바이스 하나로 알 수 있다.
   *      -> ProxyFactory 사용 시, Advisor는 필수
   *
   * </pre>
   */
  @DisplayName("Advisor Test")
  @Test
  void advisor() {

    ServiceInterface target = new ServiceImpl();
    ProxyFactory proxyFactory = new ProxyFactory(target);
    DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
    proxyFactory.addAdvisor(advisor);

    ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
    proxy.save();
    proxy.find();
  }


  @DisplayName("Advisor Test: 직접 만든 `PointCut` 사용 (예제-공부를 위해서, 직접만들어보는 것이지, 실제로는 구현된 것을 사용하시라.)")
  @Test
  void advisorWithCustomPointCut() {

    ServiceInterface target = new ServiceImpl();
    ProxyFactory proxyFactory = new ProxyFactory(target);
    DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(new CustomPointCut(), new TimeAdvice());
    proxyFactory.addAdvisor(advisor);

    ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
    proxy.save(); // proxy 적용됨
    proxy.find(); // proxy 미적용
  }


  /**
   * `NameMatchMethodPointcut`: 메서드 이름을 기반으로 매칭 ( 내부에서는 `PatterMatchUtils` 사용함)
   * `AnnotationsMatchingPointcut`: Annotation으로 매칭
   * (* 중요 ) `AspectJExpressionPointCut`: aspectJ 표현식으로 매칭.
   */
  @DisplayName("Advisor Test: Spring이 제공하는 `PointCut`(NameMatchMethodPointcut) 사용")
  @Test
  void advisorWithSpringPointCut() {

    ServiceInterface target = new ServiceImpl();
    ProxyFactory proxyFactory = new ProxyFactory(target);
    // =========================================================
    NameMatchMethodPointcut pointCut = new NameMatchMethodPointcut();
    pointCut.setMappedName("save"); // 특정 메서드만적용시길 원할 경우, 이름을 넣어주자.
    // =========================================================
    DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointCut, new TimeAdvice());
    proxyFactory.addAdvisor(advisor);

    ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();
    proxy.save();
    proxy.find();
  }


  /**
   * <h1>PointCut 생성 조건</h1>
   * <pre>
   *   @Override 대상인 getClassFilter, getMethodMatcher 모두 True를 반환해아함.
   * </pre>
   */
  static class CustomPointCut implements Pointcut {

    @Override
    public ClassFilter getClassFilter() {
      return ClassFilter.TRUE;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
      return new CustomMethodMatcher();
    }


  }

  static class CustomMethodMatcher implements MethodMatcher {

    private final String targetMethodName = "save";


    @Override
    public boolean matches(Method method, Class<?> targetClass) {
      boolean result = method.getName().equals(targetMethodName);// 해당 이름을 가진 Method 만 적용시키도록
      log.info("Call PointCut method={}, targetClass={}", method, targetClass);
      log.info("Result of PointCut > result={}", result);
      return result; // ! false일 경우, Proxy 적용되지 않음.
    }


    // isRuntime: 무시해도됨
    @Override
    public boolean isRuntime() {
      return false; // 무시해도됨
    }

    // matches: 무시해도됨
    @Override
    public boolean matches(Method method, Class<?> targetClass, Object... args) {
      return false;
    }
  }

}
