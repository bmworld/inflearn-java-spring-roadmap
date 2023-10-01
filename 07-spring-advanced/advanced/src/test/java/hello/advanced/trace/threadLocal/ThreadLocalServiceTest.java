package hello.advanced.trace.threadLocal;

import hello.advanced.trace.threadLocal.code.ThreadLocalService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static hello.util.customUtils.sleep;

@Slf4j
public class ThreadLocalServiceTest {
  private ThreadLocalService service = new ThreadLocalService();

  @Test
  @DisplayName("Thread 동시성 X Ver. (Thread Local 사용으로 동시성 문제 해결)")
  public void threadLocalWithoutConcurrencyIssue() {
    log.info("Main Start");
    Runnable userA = ()-> {
      service.logic("userA");
    };

    Runnable userB = ()-> {
      service.logic("userB");
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
  @DisplayName("Thread 동시성 Ver. (Thread Local 사용으로 동시성 문제 해결 - 해당 Thread에서 저장한 값만 조회됨) ")
  public void threadLocalWithConcurrencyIssue() {
    log.info("Main Start");
    Runnable userA = ()-> {
      service.logic("userA");
    };

    Runnable userB = ()-> {
      service.logic("userB");
    };


    Thread threadA = new Thread(userA);
    threadA.setName("thread-A");

    Thread threadB = new Thread(userB);
    threadB.setName("thread-B");


    threadA.start();
    sleep(100);
    // 동시성 문제가 발생하지 안혿록 하마.
    threadB.start();



    sleep(3000); // 메인 쓰레드 종료 대기

    log.info("Main Exit");

    /**
     * [main] Main Start
     * [thread-A] ThreadLocalService - 저장 name =userA -> storedName =null
     * [thread-B] ThreadLocalService - 저장 name =userB -> storedName =null
     * [thread-A] ThreadLocalService - 조회 storedName =userA
     * [thread-B] ThreadLocalService - 조회 storedName =userB
     * [main] Main Exit
     */
  }


}
