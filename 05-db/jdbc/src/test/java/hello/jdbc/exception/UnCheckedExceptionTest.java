package hello.jdbc.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * <h1>Unchecked Exception</h1>
 * <h2>=> 예외를 잡거나, 던지지 않아도 된다.</h2>
 * <pre>
 *   - Unchecked 상속받는 Excpetion=> 코드 상에 throw 를 사용하지 않아도 된다.
 *     => 예외를 try catch 않을 경우 ? 자동으로 밖으로 throws 처리함.
 *   - Checked Excpetion: 컴파일 시점에 처리할 수 있도록 반드시, throw or try catch 처리 해야함.
 *
 *
 *   * 장점: 개발자가 신경쓰고 싶지 않은 Unchecked Excpetion을 생략/무시할 수 있다.
 *          즉 신경쓰고 싶지 않은 예외의 의존관계를 참조하지 않아도 된다.
 *    (`Checked Exception`은 처리할 수 없는 예외를 밖으로 던지려면 항상 `throws Excpetion`을 `선언`해야한다.)
 *
 *
 *    * 단점: 개발자가 실수로 예외를 누락할 수 있다.
 *    ( 반면, `Checked Excpetion`은 Excpetion이 누락된 경우 `Compiler`가 해당 Exception을 잡아준다.)
 *
 * </pre>
 *
 */
@Slf4j
public class UnCheckedExceptionTest {


  @DisplayName("unchecked_catch")
  @Test
  void unchecked_catch () {
    Service service = new Service();
    service.callCatch(); // try catch 로 잡아줬기 때문에, Test Method 까지 Excpetion이 올라오지 않는다.

  }


  @DisplayName("unchecked_throw")
  @Test
  void unchecked_throw () {
    Service service = new Service();
    // =================================================================
//    service.callThrow(); // Test Method 까지 Throws 된다. => TEST 실패가 된다.
    // =================================================================
    Assertions.assertThatThrownBy(() -> service.callThrow())
      .isInstanceOf(MyUnCheckedException.class);

  }

  static class MyUnCheckedException extends RuntimeException {
    public MyUnCheckedException(String message) {
      super(message);
    }
  }



  static class Service {

    /**
     * Unchecked Excpetion은 try catch를 명시적으로 사용하지 않아도 된다.
     * Why? 자동으로 throws 처리한다.
     */
    Repository repository = new Repository();
    public void callCatch() {

      try {
        repository.call();
      } catch (MyUnCheckedException e) {
        log.info("MyUnCheckedException, message={}", e.getMessage(), e);
      }

    }


    /**
     * Unchecked Exception: Checked Exception 과는 다르게,  throws 예외 선언을 하지 않아도 된다.
     */
    public void callThrow() {
      repository.call();
    }

  }


  static class Repository {
    // Checked Excpetion은, Compile 시점에 Code 상으로 처리해줘야만 한다.
    public void call() {
      throw new MyUnCheckedException("---- MyUnCheckedException");
    }
  }





}
