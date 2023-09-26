package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

/**
 * <h1>Transaction 초기화 시점</h1>
 */
@SpringBootTest
@Slf4j
public class InitTransactionTest {

  @Autowired
  private Hello hello; // Transaction 이 적용되어있으므로, `Proxy` 객체를 주입받는다.!!

  @Test
  void initTest() {
    // 이 시점에서 @PostConstruct 로 인해, Spring Init 시점에 호출된 `Hello > init()` 메서드 결과
    /**
     * < 실행 결과 >
     * atSpringInit > @PostConstruct > transaction > isActiveTx = false
     */

  }


  @DisplayName("스프링 초기화 후, AOP & Container 모두 만든 이후에 호출되는 @Transactional + public 메서드를 사용하는 객체는 Proxy Instance 주입된다.")
  @Test
  void manualCallTest() {

    hello.afterSpringInit(); // 직접 호출시에는 Proxy객체를 호출받기 떄문에, Transaction 적용 된다.

    /**
     * < 실행 결과 >
     * afterSpringInit > @PostConstruct > transaction > isActiveTx = false  // <-------- Spring Init 시점, @PostConstruct 로 인함.
     * Getting transaction for [hello.springtx.apply.InitTransactionTest$Hello.initV1]
     * //================= AOP / Spring Container 완성 ==========
     * afterSpringInit > @PostConstruct > transaction > isActiveTx = true // <-------- Spring Init 이후, Proxy 객체가 주입받은 이후에는 Transaction 적용됨.
     */

    Class<? extends InitTransactionTest.Hello> proxyInstance = hello.getClass();
    Assertions.assertThat(proxyInstance.getName()).contains("$$EnhancerBySpringCGLIB$$");

  }


  @TestConfiguration
  static class TestConfig {
    @Bean
    Hello hello() {
      return new Hello();
    }
  }
  @Slf4j
  static class Hello {
    @PostConstruct // Spring Bean 초기화 시, 아래 메서드가 자동 실행된다.
    @Transactional
    public void atSpringInit() {
      boolean isActiveTx = TransactionSynchronizationManager.isActualTransactionActive();
      log.info("atSpringInit > @PostConstruct > transaction > isActiveTx = {}", isActiveTx);
    }


    @EventListener(ApplicationReadyEvent.class) // Spring Bean 초기화 시, 아래 메서드가 자동 실행된다.
    @Transactional
    public void afterSpringInit() {
      boolean isActiveTx = TransactionSynchronizationManager.isActualTransactionActive();
      log.info("afterSpringInit > @PostConstruct > transaction > isActiveTx = {}", isActiveTx);
    }

  }

}
