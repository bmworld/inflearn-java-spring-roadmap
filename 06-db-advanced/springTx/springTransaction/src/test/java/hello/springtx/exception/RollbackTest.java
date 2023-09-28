package hello.springtx.exception;

import hello.springtx.exception.RollbackTest.RollbackService.MyException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 * <h1>Exception & Transaction - Commit or Rollback Case</h1>
 * <pre>
 *   Checked Exception: `Commit` (Business 의미가 있을 떄 사용)
 *   Unchecked(Runtime) Exception: `Rollback` (복구 불가능한 예외)
 * </pre>
 *
 * <h1>왜 Spring은 Checked EX는 커밋, Unchecked Ex는 Rollback?</h1>
 * <pre>
 *   - 스프링은 기본적으로 Checked Ex는 비지니스적 의미가 있을 때 사용하고,
 *     Unchecked Ex는 복구 불가한 예외로 규정한다.
 * </pre>
 */
@SpringBootTest
public class RollbackTest {

  @Autowired
  private RollbackService service;

  @Test
  @DisplayName("Runtime Ex ( 결과: Rollback)")
  public void runtimeEx() {
    assertThatThrownBy(()-> service.runtimeException())
      .isInstanceOf(RuntimeException.class);
    // Rollback or Commit 여부 확인 => application.yml 설정 필요.

    /**
     * 1. Transaction 시작 => Creating new transaction with name [hello.springtx.exception.RollbackTest$RollbackService.runtimeException]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
     * ... Exception 발생
     * 2. Transaction 끝 => Initiating transaction rollback
     */

  }


  @Test
  @DisplayName("Checked Ex ( 결과: Commit)")
  public void checkedEx() {
    assertThatThrownBy(()-> service.checkedException())
      .isInstanceOf(MyException.class);
    // Rollback or Commit 여부 확인 => application.yml 설정 필요.

    /**
     * 1. Transaction 시작 => Creating new transaction with name [hello.springtx.exception.RollbackTest$RollbackService.checkedException]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
     * ... Exception 발생
     * 2. Transaction 끝 => Initiating transaction commit
     */

  }




  @Test
  @DisplayName("Checked Ex And 특정 Ex 발생 시 Rollback ( 결과: Rollback)")
  public void rollbackFor() {
    assertThatThrownBy(()-> service.rollbackFor())
      .isInstanceOf(MyException.class);
    // Rollback or Commit 여부 확인 => application.yml 설정 필요.

    /**
     * 1. Transaction 시작 => Creating new transaction with name [hello.springtx.exception.RollbackTest$RollbackService.rollbackFor]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT,-hello.springtx.exception.RollbackTest$RollbackService$MyException
     * ... Exception 발생
     * 2. Transaction 끝 => Initiating transaction rollback
     */

  }

  @TestConfiguration
  static class RollbackTestConfig {

    @Bean
    public RollbackService rollbackService() {
      return new RollbackService();
    }
  }


  @Slf4j
  static class RollbackService{
    // 런타임 예외 발생: Rollback
    @Transactional
    public void runtimeException() {
      log.info("RollbackService > Call runtimeException");
      throw new RuntimeException();
    }


    @Transactional
    public void checkedException() throws MyException {
      log.info("RollbackService > Call checkedException");
      throw new MyException(); // Checked Ex 발생: Commit
    }

    // Checked Exception
    @Transactional(rollbackFor = MyException.class)
    public void rollbackFor() throws MyException {
      log.info("RollbackService > Call checked Exception and rollbackFor MyException");
      throw new MyException();

    }



    static class MyException extends Exception {
    }
  }
}
