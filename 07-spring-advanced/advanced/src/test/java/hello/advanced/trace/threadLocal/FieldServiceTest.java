package hello.advanced.trace.threadLocal;

import hello.advanced.trace.threadLocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hello.util.customUtils.sleep;

@Slf4j
public class FieldServiceTest {
  private FieldService fieldService = new FieldService();

  @Test
  @DisplayName("동시성 이슈 없는 Ver. (각 쓰레드가 중복되지 않음)")
  public void threadLocalWithoutConcurrencyIssue() {
    log.info("Main Start");
    Runnable userA = ()-> {
      fieldService.logic("userA");
    };

    Runnable userB = ()-> {
      fieldService.logic("userB");
    };


    Thread threadA = new Thread(userA);
    threadA.setName("thread-A");

    Thread threadB = new Thread(userB);
    threadB.setName("thread-B");


    threadA.start();
    sleep(2000);
    // 동시성 문제가 발생하지 안혿록 하마.
    threadB.start();
    sleep(2000); // 메인 쓰레드 조욜 대기

    log.info("Main Exit");
  }



  @Test
  @DisplayName("동시성 이슈 Ver. (Thread가 2개 이상 동시에 작동할 경우)")
  public void threadLocalWithConcurrencyIssue() {
    log.info("Main Start");
    Runnable userA = ()-> {
      fieldService.logic("userA");
    };

    Runnable userB = ()-> {
      fieldService.logic("userB");
    };


    Thread threadA = new Thread(userA);
    threadA.setName("thread-A");

    Thread threadB = new Thread(userB);
    threadB.setName("thread-B");


    threadA.start();
    sleep(800);
    // 동시성 문제가 발생하지 안혿록 하마.
    threadB.start();
    sleep(800); // 메인 쓰레드 조욜 대기

    log.info("Main Exit");

    /**
     * [main] Main Start
     * [thread-A] FieldService - 저장 name =userA -> storedName =null
     * [thread-B] FieldService - 저장 name =userB -> storedName =userA
     * [main] Main Exit
     */
  }


}
