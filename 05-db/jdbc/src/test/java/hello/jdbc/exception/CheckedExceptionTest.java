package hello.jdbc.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


/**
 * <h1>Check vs Unchecked Exception 기준: `Compiler`</h1>
 *
 * <pre>
 *  * Exception 종류
 *  1. Checked Exception:  IOException, (RuntimeException 제외하고, Exception 상속받는 모든 Child Exception)
 *  2. Unchecked Exception: RuntimeException
 *
 *
 *  * Checked Excpetion: 예외를 잡아서 처리 또는 밖으로 던지도록 선언해야함.
 *    ㄴ 그렇지 않을 경우? Compile Error 발생
 * </pre>
 *
 */

@Slf4j
public class CheckedExceptionTest {


  @DisplayName("checked catch test")
  @Test
  void checked_catch () {
    Service service = new Service();
    service.callCatch();

  }


  /**
   * 체크예외를 밖으로 던지느 코드
   * 체크 예외는 예외를 잡지 않고 밖으로 던지면 throws Exception 예외를 메서드에 필수로 선언해야함.
   */
  @DisplayName("checked throw test")
  @Test
  void checked_throw () {

    Service service = new Service();
    Assertions.assertThatThrownBy(() -> service.callThrow())
      .isInstanceOf(MyCheckedException.class);


  }


  /**
   * Exception 상속받는 예외는 Checked Exception
   */
  static class MyCheckedException extends Exception {
    public MyCheckedException(String message) {
      super(message);
    }
  }


  /**
   * <h1>Checked Excpetion</h1>
   * : 예외를 잡아서 처리 또는 던지느 것 중 하나를 택해야 한다.
   */

  static class Service{
    Repository repository = new Repository();

    /**
     * <h3>Exception with catch</h3>
     * <pre>
     * : 예외를 잡아서 처리하는 코드
     * </pre>
     *
     */
    public void  callCatch() {
      try {
        repository.call();
      } catch (MyCheckedException e) {
        log.info("Caught exception, message={}", e.getMessage(), e);
      }

    }


    /**
     *
     *  <h3>Unchecked Exception with catch</h3>
     *  <pre>
     *  : 체크예외를 밖으로 던지느 코드
     *    체크 예외는 예외를 잡지 않고 밖으로 던지면 throws Exception 예외를 메서드에 필수로 선언해야함.
     *  </pre>
     *
     *
     */
    public void callThrow() throws MyCheckedException {
      repository.call();
    }

  }

  static class Repository {
    // Checked Excpetion은, Compile 시점에 Code 상으로 처리해줘야만 한다.
    public void call() throws MyCheckedException {
      throw new MyCheckedException("---- MyCheckedException !!!");
    }
  }




}
