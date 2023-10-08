package hello.proxy.config.v5_autoProxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyFactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <h1>자동 Proxy 생성</h1>
 * <pre>
 *   - Spring Bean 등록된 모든 `Advisor`를 찾아서, 해당되는 method를 가진 Class를 Proxy 적용해서 해당 Proxy Bean을 Spring에 자동 등록해준다.
 *     => Spring에 @BeanPostProcessor (자동 Proxy 생성기)가 이미 Bean으로 등록되어있다.
 *   - Dependency: `aspectJ` (org.springframework.boot:spring-boot-starter-app)
 * </pre>
 */
@Configuration
@Import({AppV1Config.class, AppV2Config.class}) // 예제에서 이미 등록한 interface Class & Concrete Class 을 추가한 Config를 가져온다.
public class AutoProxyConfig {

  /**
   * <h1>해당 advisor 사용 시, setMappedNames 내에 포함되는 AppConfig에 설정된 Bean까지 등록되는 ISSUE가 발생한다.  </h1>
   */
//  @Bean
//  public Advisor advisor1(LogTrace logTrace) {
//    // pointcut
//    NameMatchMethodPointcut pointCut = new NameMatchMethodPointcut();
//    pointCut.setMappedNames("request*", "order*", "save*"); // 적용대상 method 패턴을 지정할 수 있다.
//    // advice
//    LogTraceAdvice advice = new LogTraceAdvice(logTrace);
//    return new DefaultPointcutAdvisor(pointCut, advice);
//  }


  /**
   * <h1>[실무용] AspectJ라는 AOP에 특화된 PointCut 표현식을 적용한다.</h1>
   * <pre>
   *   목적: 예상치 못한 Bean에 AOP 적용 방지 => 정밀하게 AOP 대상 Method 지정
   * </pre>
   * <h1>해당 문법의 ISSUE: no-log인 메서드에도 적용됨</h1>
   */
//  @Bean
//  public Advisor advisor2(LogTrace logTrace) {
//    // pointcut => AspectJ
//    AspectJExpressionPointcut pointCut = new AspectJExpressionPointcut();
//    pointCut.setExpression("execution(* hello.proxy.app..*(..))");
//    /**
//     * * AspectJ Expression 문법 설명
//     * - `*`: 모든 반환 타입
//     * - `hello.proxy.app..`: 대상 패키지 및 그 하위 패키지
//     * - `*(..)`: `*` 모든 메서드 이름 / `(..)` parameter 는 상관없다는 의미
//     */
//
//
//
//    // advice
//    LogTraceAdvice advice = new LogTraceAdvice(logTrace);
//    return new DefaultPointcutAdvisor(pointCut, advice);
//  }


  /**
   * <h1>Advanced Example: `no-log` 메서드는 AOP 대상에서 제외</h1>
   */
  @Bean
  public Advisor advisor3(LogTrace logTrace) {
    // pointcut => AspectJ
    AspectJExpressionPointcut pointCut = new AspectJExpressionPointcut();
    pointCut.setExpression("execution(* hello.proxy.app..*(..)) && !execution(* hello.proxy.app..noLog(..))");
    /**
     * * AspectJ Expression 문법 설명
     * - `*`: 모든 반환 타입
     * - `hello.proxy.app..`: 대상 패키지 및 그 하위 패키지
     * - `*(..)`: `*` 모든 메서드 이름 / `(..)` parameter 는 상관없다는 의미
     */



    // advice
    LogTraceAdvice advice = new LogTraceAdvice(logTrace);
    return new DefaultPointcutAdvisor(pointCut, advice);
  }
}
