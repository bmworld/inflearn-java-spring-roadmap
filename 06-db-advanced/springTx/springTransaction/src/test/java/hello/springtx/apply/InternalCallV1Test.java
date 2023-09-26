package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.transaction.Transactional;

/**
 * <h1>Proxy & 내부호출(Internal) 주의사항 => Transaction 미적용 CASE</h1>
 * <pre>
 *   < 흐름 >
 *   1. 클라이언트 Test code는 `callService.external()` 호출 -> 여기서 callService는 Transaction Proxy이다.
 *   2. `callService`의 `Transaction Proxy` 호출됨.
 *   3. `callService.external()` 메서드에는 `@Transactional` 없음 -> 따라서, Transaction Proxy는 Transaction을 적용하지 않음
 *   4. 따라서, 이때 `callService`는 Transaction 미적용 상태인 실제 `callService` Object Instance의 `external()` 메서드를 호출한다.
 *   5. `external()` 메서드는 내부에서 Proxy를 거치지 않은, 해당 Instance의 `internal()` 메서드를 호출한다. 여기서 문제가 발생한다.
 *
 *   => Proxy를 적용하려면, Proxy 객체의 internal()을 사용해야한다!
 *
 *   < 문제의 원인 >
 *   Java에서 method 앞에 별도의 참조가 없으면, `this`라는 뜻으로 자기자신의 Instance를 가리킨다.
 *   결과적으로 자기 자신의 내부 메서드를 호출하는 `this.internal()`이 호출되며,
 *   this는 자기 자신이므로 실제 대상객체(target)의 instance를 뜻한다.
 *
 *   간단히 말해서,
 *   this 를 통한 내부호출은
 *   Proxy를 거치지 않고,
 *   그에 따라서,
 *   Transaction을 적용할 수 없다.
 *
 * </pre>
 *
 * <h1>Transaction - Public Method에만 적용됨</h1>
 * <pre>
 *   Spring Transaction AOP 기능은 `public` Method에만 Transaction을 적용하도록 기본설정됨.
 *
 *   `public`아닌 method에 @Transactional 붙어있으면, Exception 발생하지는 않는다......다만 Transaction 이 무시될 뿐
 * </pre>
 */
@SpringBootTest
@Slf4j
public class InternalCallV1Test {

  @Autowired
  CallService callService; // Transaction 이 적용되어있으므로, `Proxy` 객체를 주입받는다.!!

  @DisplayName("Transaction 사용 시, Proxy Bean 주입여부 검증")
  @Test
  void validateProxyBeanWithTransactionAnnotationMethod() {
    Class<? extends CallService> proxyInstance = callService.getClass();
    log.info("CallService class={}", proxyInstance); // 결과: class hello.springtx.apply.InternalCallV1Test$CallService$$EnhancerBySpringCGLIB$$308805c
    Assertions.assertThat(proxyInstance.getName()).contains("$$EnhancerBySpringCGLIB$$");

  }

  @DisplayName("internalCall")
  @Test
  void internalCall() {
    callService.internal();
    /**
     *  결과
     *  Getting transaction for [hello.springtx.apply.InternalCallV1Test$CallService.internal]
     *  Call internal
     *  TX active = true <===================== 트랜잭션 적용됨
     *  Tx readOnly = false
     *  Completing transaction for [hello.springtx.apply.InternalCallV1Test$CallService.internal]
     */

  }


  @DisplayName("externalCall: Transaction 작동 안 하는 CASE")
  @Test
  void externalCall() {
    callService.external();
    /**
     *  Call external
     *  TX active = false
     *  Tx readOnly = false
     *  Call internal
     *  TX active = false <------------------- @Transactional Annotation 있음에도... 트랜잭션 적용 안 됨.**
     *  Tx readOnly = false
     */

  }
  @TestConfiguration
  static class InternalCallV1TestConfig {
    @Bean
    CallService callService() {
      return new CallService();
    }
  }
  @Slf4j
  public static class CallService {

    public void external() {
      log.info("Call external");
      prtTxInfo();
      /**
       * <Proxy 방식의 AOP 한계>
       * external() 메서드가 호출될 경우, `Proxy CallService`에서 호출되지 않으므로,
       * 아래 메서드는 this.internal() 가 된다.
       * 즉, Proxy가 아닌, `실제 객체(this)`의 메서드가 호출되므로,
       * 본 메서드에 @Transaction 존재 유무와 관계없이, Transaction이 적용되지 않는다.
       * ==============> Proxy 객체의 internal() 호출 시, Transaction 적용된다.
       */
      // =================================================================
      internal();
      // =================================================================
    }

    @Transactional
    public void internal() {

      log.info("Call internal");
      prtTxInfo();


    }


    private void prtTxInfo() {
      boolean isTxActive = TransactionSynchronizationManager.isActualTransactionActive();
      log.info("TX active = {}", isTxActive);
      boolean isTxReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
      log.info("Tx readOnly = {}", isTxReadOnly);
    }
  }

}
