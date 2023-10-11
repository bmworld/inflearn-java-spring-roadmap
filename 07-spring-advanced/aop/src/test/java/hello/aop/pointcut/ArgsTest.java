package hello.aop.pointcut;


import hello.aop.member.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h1>args</h1>
 * <pre>
 *   - 인자가 주어진 타입의 Instance인 Join point로 매칭
 *   - 기본 문법은 `execution`의 `args` 부분과 동일
 *
 *   < execution VS args >
 *   - `execution`: 부모타입 허용 X (parameter Type이 정확하게 매칭 필요)
 *   - `args`: 부모 타입 허용 (실제 넘어온 parameter 객체 instance를 통해 판단)
 *
 *
 * </pre>
 */
public class ArgsTest {

  private Method helloMethod;

  @BeforeEach
  public void init() throws NoSuchMethodException {
    helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
  }

  private AspectJExpressionPointcut pointcut(String expression) {
    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    pointcut.setExpression(expression);
    return pointcut;
  }

  @Test
  void args() {
    //hello(String)과 매칭
    assertThat(pointcut("args(String)")
      .matches(helloMethod, MemberServiceImpl.class)).isTrue();
    assertThat(pointcut("args(Object)")
      .matches(helloMethod, MemberServiceImpl.class)).isTrue();
    assertThat(pointcut("args()")
      .matches(helloMethod, MemberServiceImpl.class)).isFalse();
    assertThat(pointcut("args(..)")
      .matches(helloMethod, MemberServiceImpl.class)).isTrue();
    assertThat(pointcut("args(*)")
      .matches(helloMethod, MemberServiceImpl.class)).isTrue();
    assertThat(pointcut("args(String,..)")
      .matches(helloMethod, MemberServiceImpl.class)).isTrue();
  }

  /**
   * execution(* *(java.io.Serializable)): 메서드의 시그니처로 판단 (정적)
   * args(java.io.Serializable): 런타임에 전달된 인수로 판단 (동적)
   */
  @Test
  void argsVsExecution() {
    //Args
    assertThat(pointcut("args(String)")
      .matches(helloMethod, MemberServiceImpl.class)).isTrue();
    assertThat(pointcut("args(java.io.Serializable)")
      .matches(helloMethod, MemberServiceImpl.class)).isTrue();
    assertThat(pointcut("args(Object)")
      .matches(helloMethod, MemberServiceImpl.class)).isTrue();

    //Execution
    assertThat(pointcut("execution(* *(String))")
      .matches(helloMethod, MemberServiceImpl.class)).isTrue();
    assertThat(pointcut("execution(* *(java.io.Serializable))") //매칭 실패 (부모 타입 허용 안 함)
      .matches(helloMethod, MemberServiceImpl.class)).isFalse();
    assertThat(pointcut("execution(* *(Object))") //매칭 실패 (부모 타입 허용 안 함)
      .matches(helloMethod, MemberServiceImpl.class)).isFalse();
  }


}
