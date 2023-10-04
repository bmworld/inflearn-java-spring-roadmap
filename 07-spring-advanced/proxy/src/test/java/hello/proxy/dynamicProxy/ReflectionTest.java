package hello.proxy.dynamicProxy;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <h1>Reflection</h1>
 * <pre>
 *   * 의미: Class 또는 Method 의  Meta 정보를 사용하여
 *     동적으로 호출하는 Method를 변경할 수 있는 기술.
 * </pre>
 * <h1>주의사항</h1>
 * <pre>
 *   * Reflection은 일반적으로 사용하면 안 된다.
 *     프로그래밍 언어의 발달 양상은 Type을 기반으로 Compile 시점에 오류를 잡는 것을 중심으로,
 *     개발자의 개발 편의성을 증대시켜왔다.
 *
 *     그러나, Reflection 은 Compile 시점에 오류를 잡지 못하므로,
 *     '일반적인 공통처리가 필요할 때' 제한적으로 사용해야 하는 것이 좋다.
 *
 *     ex. Framework 개발
 * </pre>
 */
@Slf4j
public class ReflectionTest {

  @DisplayName("JDK reflection test")
  @Test
  void reflection() {
    Hello target = new Hello();


    // 공통 로직 1 시작
    log.info("Start reflection test");
    String result1 = target.callA();
    log.info("result = {}", result1);
    // 공통 로직 1 끝


    // 공통 로직 2 시작
    log.info("Start reflection test 2");
    String result2 = target.callB();
    log.info("result = {}", result2);
    // 공통 로직 2 끝

  }

  @DisplayName("reflection을 사용하여, Method 정보를 동적으로 획득할 수 있다.")
  @Test
  void reflection_1() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    // Reflection을 활용한 Class 정보 획득
    Class helloClass = Class.forName("hello.proxy.dynamicProxy.ReflectionTest$Hello");
    Hello target = new Hello();

    //callA Method 정보
    Method methodCallA = helloClass.getMethod("callA");
    Object result1 = methodCallA.invoke(target); // 획득한 Method Meta Info를 사용하여, 실제 Instance의 Method를 호출한다.
    log.info("result = {}", result1);


    //callA Method 정보
    Method methodCallB = helloClass.getMethod("callB");
    Object result2 = methodCallB.invoke(target); // target Instance 에 존재하는 callA를 호출
    log.info("result = {}", result2);

  }


  @DisplayName("reflection을 사용하여, Method 정보를 동적으로 획득할 수 있다.")
  @Test
  void reflection_2() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    // Reflection을 활용한 Class 정보 획득
    Class helloClass = Class.forName("hello.proxy.dynamicProxy.ReflectionTest$Hello");
    Hello target = new Hello();

    //callA Method 정보
    Method methodCallA = helloClass.getMethod("callA");
    dynamicCall(methodCallA, target);
    Method methodCallB = helloClass.getMethod("callB");
    dynamicCall(methodCallB, target);

  }

  /**
   *
   * @param method 호출할 메서드 정보 (**이것이 핵심)
   * @param target 실제 실행할 Instance 정보가 넘어온다. 타입이 Object라는 것은 어떠한 instance도 받을 수 있다는 의미.
   */
  private void dynamicCall(Method method, Object target) throws InvocationTargetException, IllegalAccessException {
    log.info("Start dynamicCall");
    Object result = method.invoke(target);
    log.info("result = {}", result);
  }

  @Slf4j
  static class Hello {
    public String callA() {
      log.info("call A");
      return "A";
    }

    public String callB() {
      log.info("call B");
      return "B";
    }
  }
}
