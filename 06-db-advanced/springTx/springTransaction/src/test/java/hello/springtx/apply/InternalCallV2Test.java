package hello.springtx.apply;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;

/**
 * <h1>Proxy & 내부호출(Internal) 주의사항 해결방법 => Proxy적용대상 메서드를 별도로 분리</h1>
 * <pre>
 *   < 흐름 >
 *    1. 클라 테스트코드는 `callService.external()` 호출
 *    2. `callService`: 실제 callService의 Object Instance.
 *    3. `callService`는 주입받은 `internalService.internal()` 호출한다.
 *    4. `internalService`는 @Transactional을 가진 internal을 호출했으므로, Proxy Object이다.
 *      =?
 *
 * </pre>
 */
@SpringBootTest
@Slf4j
public class InternalCallV2Test {

  @Autowired
  CallService callService; // Transaction 이 적용되어있으므로, `Proxy` 객체를 주입받는다.!!

  @DisplayName("Transaction 사용 시, Proxy Bean이 아니다. (@Transactional 없으므로)")
  @Test
  void printProxyBeanByTransaction() {
    //
    Class<? extends CallService> nonProxyInstance = callService.getClass();
    log.info("CallService class={}", nonProxyInstance); // 결과: class hello.springtx.apply.InternalCallV2Test$CallService
    // IF) @Transactional 적용되었다면, class hello.springtx.apply.InternalCallV2Test$CallService$$EnhancerBySpringCGLIB$$c2dd06f8
    Assertions.assertThat(nonProxyInstance.getName()).doesNotContain("$$EnhancerBySpringCGLIB$$");

  }


  @DisplayName("externalCall: Transaction 작동 CASE (Transaction적용되는 Proxy 객체를 별도로 분리하여, Transaction 코드가 없는 메서드를 가진 Bean이 Proxy객체가 아니더라도, 해당 @Transactoin을 가진 메서드의 Transaction이 정상적으로 적용되도록 한다.)")
  @Test
  void externalCallV2() {
    callService.external();
    /**
     * Call external
     * TX active = false
     * Tx readOnly = false
     * Getting transaction for [hello.springtx.apply.InternalCallV2Test$InternalCallService.internal]
     * Call InternalCallService > internal
     * TX active = true
     * Tx readOnly = false
     * Completing transaction for [hello.springtx.apply.InternalCallV2Test$InternalCallService.internal]
     */
  }


  @TestConfiguration
  static class InternalCallV2TestConfig {
    @Bean
    CallService callService() {
      return new CallService(internalCallService());
    }

    @Bean
    InternalCallService internalCallService() {
      return new InternalCallService();
    }
  }
  @Slf4j
  @RequiredArgsConstructor
  public static class CallService {

    private final InternalCallService internalCallService;
    public void external() {
      log.info("Call external");
      prtTxInfo();
      internalCallService.internal();
    }

  }

  @Slf4j
  public static class InternalCallService {
    @Transactional
    public void internal(){
      log.info("Call internal");
      prtTxInfo();
    }

  }
  private static void prtTxInfo() {
    boolean isTxActive = TransactionSynchronizationManager.isActualTransactionActive();
    log.info("TX active = {}", isTxActive);
    boolean isTxReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
    log.info("Tx readOnly = {}", isTxReadOnly);
  }

}
