package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h1>WithinMethod</h1>
 * <pre>
 *   - execution과는 달리, Pointcut 위치 지정이 정.확.해야 함
 * </pre>
 */
@Slf4j
public class WithinTest {
  private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
  private Method myMethod;

  @BeforeEach
  public void init() throws NoSuchMethodException {
    myMethod = MemberServiceImpl.class.getMethod("hello", String.class); // (메서드명, 반환타입)
  }

  @Test
  void withinExact() {
    pointcut.setExpression("within(hello.aop.member.MemberServiceImpl)");
    assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void withinStar() {
    pointcut.setExpression("within(hello.aop.member.*Service*)");
    assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  void withinSubPackage() {
    pointcut.setExpression("within(hello.aop..*)");
    assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  @DisplayName("타켓의 타입에만 직접 적용, 인터페이스를 선정하면 안된다.")
  void withinSuperTypeFalse() {
    pointcut.setExpression("within(hello.aop.member.MemberService)");
    assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isFalse();
  }

  @Test
  @DisplayName("execution은 타입 기반, 인터페이스 선정 가능")
  void executionSuperTypeTrue() {
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }
}
