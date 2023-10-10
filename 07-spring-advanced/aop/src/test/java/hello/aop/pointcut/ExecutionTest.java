package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

/**
 * <h1>Execution 지시자</h1>
 * <h2>매칭 조건</h2>
 * <pre>
 * 접근제어자?: `public`
 * 반환타입: `String`
 * 선언타입?: `hello.aop.member.MemberServiceImpl`
 * 메서드이름: `hello`
 * 파라미터: `(String)`
 * 예외?: (..생략)
 * </pre>
 *
 * <h2>Parameter 규칙</h2>
 * <pre>
 *   `(String)`: 정확하게 String Type Param
 *   `()`: No Param
 *   `(*)`:  정확히 하나의 Param & 모든 타입 허용
 *   `(*, *)`: 정확히 두 개의 Param & 각 모든 타입 허용
 *   `(..)`: 숫자와 무관한 모든 Param & 모든 Type 허용 (* 파라미터가 없어도 된다 = `0..*`)
 *   `(String, ..)`: String Type이 첫 번째 Args이고, 숫자와 무관한 모든 Param & Type 허용
 * </pre>
 */
@Slf4j
public class ExecutionTest {

  private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
  private Method myMethod;

  @BeforeEach
  public void init() throws NoSuchMethodException {
    myMethod = MemberServiceImpl.class.getMethod("hello", String.class); // (메서드명, 반환타입)

  }

  @Test
  @DisplayName("print method")
  public void printMethod() {
    log.info("myMethod = {}", myMethod); // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
  }

  @Test
  @DisplayName("매칭: 가장 정확한 조건")
  void exactMatch() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue(); // pointcut을 적용한 method 가 TargetClass 내에 존재하는지 검증.
  }


  @Test
  @DisplayName("매칭: 가장 많은 생략")
  void allMatch() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* *(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  @DisplayName("매칭: 메서드 이름 일치")
  void nameMatch() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* hello(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }


  @Test
  @DisplayName("매칭: 메서드 이름 일치 - 패턴 매치")
  void nameMatchByPatternMatch() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* hel*(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  @DisplayName("매칭: 메서드 이름 일치 - 패턴 매치2")
  void nameMatchByPatternMatch2() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* *el*(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  @DisplayName("매칭: 메서드 이름 일치 - 패턴 매치2")
  void nameMatch_failed() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* *nonono*(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isFalse();
  }


  // =================================================================
  // ================================================================= 패키지 정확히 매칭
  // =================================================================
  @Test
  @DisplayName("매칭: 패키지")
  void packageMatch() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }

  @Test
  @DisplayName("매칭: 패키지 - 패턴매치")
  void packageMatchByPatternMatch() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }


  @Test
  @DisplayName("매칭 실패: 패키지 경로 오류")
  void packageMatchByPatternMatch_fail() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* hello.aop.*.*(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isFalse();
  }

  @Test
  @DisplayName("매칭: 패키지 경로 - 패턴매치 by 서브패키지 ( `..` ) ")
  void packageMatchByPatternMatch_subpackage() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* hello.aop.member..*.*(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }



  @Test
  @DisplayName("매칭: 패키지 경로 - 패턴매치 by 서브패키지 ( `..` ) 2")
  void packageMatchByPatternMatch_subpackage2() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* hello.aop..*.*(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }

  // =================================================================
  // ================================================================= Type Match
  // =================================================================
  @Test
  @DisplayName("매칭: 타입")
  void typeExactMatch() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }


  @Test
  @DisplayName("매칭: 타입 - 슈퍼타입 (부모타입 허용) (*주의: 부모타입에 존재하는 메서드만 적용 가능)")
  void typeExactSuperTypeMatch() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }


  @Test
  @DisplayName("매칭: 타입 - 부모타입에 없는 메서드 - 사용불가")
  void typeMatch_failedByNoSuperType() throws NoSuchMethodException {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);


    pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
    Assertions.assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isFalse();
  }

  @Test
  @DisplayName("매칭: 타입 - 자식 메서드 자체 검증")
  void typeMatch_successByInnerMethod() throws NoSuchMethodException {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    Method internalMethod = MemberServiceImpl.class.getMethod("internal", String.class);


    pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
    Assertions.assertThat(pointcut.matches(internalMethod, MemberServiceImpl.class)).isTrue();

  }

  // =================================================================
  // ================================================================= Parameter Matching
  // =================================================================

  @Test
  @DisplayName("매칭: 파라미터")
  void argsMatch() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* *(String))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }


  @Test
  @DisplayName("매칭: 파라미터 (파라미터가 일치하지 않음)")
  void argsMatch_failedByNoArgs() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* *())");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isFalse();
  }



  @Test
  @DisplayName("매칭: 파라미터 (정확히 하나의 파리미터 허용, 모든 타입 허용)")
  void argsMatch_allowAllArgsType() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* *(*))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }


  @Test
  @DisplayName("매칭: 파라미터 (파라미터 개수와 상관없이, 모든 타입 허용)") // ex. (), (xxx), (xxx, yyy), ...
  void argsMatch_allowAllArgsSortAndType() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* *(..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }


  @Test
  @DisplayName("매칭: 파라미터 (파라미터 개수와 상관없이, 모든 타입 허용)") // ex. (String), (String, xxx), (String, xxx, yyy), ...
  void argsMatch_allowAllArgsWithSpecifitType() {
    // TARGET
    // => public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
    pointcut.setExpression("execution(* *(String, ..))");
    Assertions.assertThat(pointcut.matches(myMethod, MemberServiceImpl.class)).isTrue();
  }
}
