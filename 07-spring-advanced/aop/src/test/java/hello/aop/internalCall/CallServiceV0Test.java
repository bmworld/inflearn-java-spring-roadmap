package hello.aop.internalCall;

import hello.aop.internalCall.aop.CallAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

/**
 * <h1>핵심: Proxy가 적용된 Target Method 중, `내부 호출(call internally)된 Method`는 Proxy 거치지 않음</h1>
 * <pre>
 *   - 결과적으로 내부호출된 메서드는 Proxy를 거치지 않으며, `Advice` 또한 적용 불가
 *   - Spring AOP의 한계
 *     (cf.AspectJ를 통하여, 컴파일 시점 또는 로드 시점에 Proxy를 적용할 수 있다면, 문제 없음)
 * </pre>
 */
@Slf4j
@SpringBootTest
@Import(CallAspect.class)
public class CallServiceV0Test {

  @Autowired
  CallServiceV0 callService; // Proxy가 Spring Container에 올라간다.

  /**
   * <h1>RESULT</h1>
   * <pre>
   * hello.aop.internalCall.aop.CallAspect    : [AOP] void hello.aop.internalCall.CallServiceV0.externalCall()
   * hello.aop.internalCall.CallServiceV0     : Called externally
   * hello.aop.internalCall.CallServiceV0     : Called internally
   * </pre>
   *
   * <h1> ! 만약, 내부 메서드에도 AOP가 적용되었다면 아래와 같은 LOG가 남았을 겻이다.</h1>
   * <pre>
   *   hello.aop.internalCall.aop.CallAspect    : [AOP] void hello.aop.internalCall.CallServiceV0.externalCall()
   *   hello.aop.internalCall.CallServiceV0     : Called externally
   *   hello.aop.internalCall.aop.CallAspect    : [AOP] void hello.aop.internalCall.CallServiceV0.internalCall()
   *   hello.aop.internalCall.CallServiceV0     : Called internally
   * </pre>
   */
  @Test
  @DisplayName("externalCall: Spring Bean에 Proxy 객체의 `내부 메서드`를 호출 시, 해당 target의 internall 메서드에는 AOP 적용 안 됨.")
  void externalCall() {
    callService.externalCall();
  }

  @Test
  @DisplayName("internalCall: Proxy에서 외부 호출된 메서드는 AOP 정상 적용됨")
  void internalCall() {
    // 이름이 internalCall 일 뿐, 외부 호출된 것이다.
    callService.internalCall();
    /**
     * hello.aop.internalCall.aop.CallAspect    : [AOP] void hello.aop.internalCall.CallServiceV0.internalCall()
     * hello.aop.internalCall.CallServiceV0     : Called internally
     */

  }
}
