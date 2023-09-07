package hello.jdbc.exception;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

/**
 * <h1>Exception 사용 시, 꼭 기존 예외를 포함시키시라.</h1>
 * <pre>
 * {@code
 *     class RuntimeSQLException extends RuntimeException {
 *      public RuntimeSQLException(Throwable cause) {
 *        super(cause);
 *      }
 *    }
 * }
 * </pre>
 *
 */

@Slf4j
public class UnCheckedAppTest {


  @DisplayName("unCheckedEx Exception")
  @Test
  void unCheckedEx() {
    Controller controller = new Controller();
    Assertions.assertThatThrownBy(controller::request).isInstanceOf(RuntimeSQLException.class);
  }


  @DisplayName("[필수 유의사항] Stack Trace: 기존 예외 포함 Ver. (public RuntimeSQLException(Throwable cause) {super(cause)})")
  @Test
  void stackTrace () {
    Controller controller = new Controller();
    try{
      controller.request();
    } catch(Exception e) {
        e.getStackTrace();
      log.info("stackTrace 기존예외 포함 Ver. > ex", e);
      // hello.jdbc.exception.UnCheckedAppTest$RuntimeSQLException: java.sql.SQLException: Hello SQLException !
    }
  }

  @DisplayName("[비추천] Stack Trace: 기존 예외 포함하지 않음 ")
  @Test
  void stackTraceWithoutSuperEx () {
    Controller controller = new Controller();
    try{
      controller.requestWithoutSuperEx();
    } catch(Exception e) {
      e.getStackTrace();
      log.info("stackTrace 기존예외 포함 Ver. > ex", e);
      /**
       * 기존 SQL Exception 내용이 보이질 않는다.
       */
    }
  }

  // =================================================================
  // =================================================================
  // =================================================================
  // =================================================================
  // =================================================================
  // =================================================================
  // =================================================================

  static class Controller {
    Service service = new Service();

    public void request() {
      service.logic();
    }

    public void requestWithoutSuperEx() {
      service.logicWithoutSuperEx();
    }
  }

  static class Service {

    Repository repository = new Repository();
    NetworkClient networkClient = new NetworkClient();

    public void logic() {
      repository.callWithSuperEx();
      networkClient.call();
    }

    public void logicWithoutSuperEx() {
      repository.callWithoutSuperEx();
      networkClient.call();
    }


  }

  static class NetworkClient {
    public void call() {
      throw new RuntimeConnectionException("Hello RuntimeConnectionException !");
    }

  }

  static class Repository {
    public void callWithSuperEx() {
      // Unchecked Ex ( = runtime Ex)

      // CASE: 기존 Exception 넣어서, 부모에게 전달
      try {
        runSQL();
      } catch (SQLException e) {
        throw new RuntimeSQLException(e);
      }
    }

    public void callWithoutSuperEx() {
      // Unchecked Ex ( = runtime Ex)
      // CASE: 기존 Exception 넣어서, 부모에게 전달
      try {
        runSQL();
      } catch (SQLException e) {
        throw new RuntimeSQLException();
      }
    }

    public void runSQL() throws SQLException {
      throw new SQLException("Hello SQLException !");
    }
  }

  static class RuntimeConnectionException extends RuntimeException {
    public RuntimeConnectionException(String message) {
      super(message);
    }
  }
  /**
   * 참조)
   * <pre>
   * Exception(Unchecked Exception) 사용 시, Throwable(cause)를 부모에게 전달하시라.
   * 예외 출력 시, Stack trace 에서 기존 예외도 함께 확인할 수 있다.
   * </pre>
   */
  static class RuntimeSQLException extends RuntimeException {


    public RuntimeSQLException(Throwable cause) {
      super(cause);
    }

    public RuntimeSQLException() {

    }
  }
}
