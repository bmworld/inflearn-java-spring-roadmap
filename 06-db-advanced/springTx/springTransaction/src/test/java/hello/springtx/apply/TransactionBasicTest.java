package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * <h1>Transaction 설명</h1>
 * <pre>
 *   `@Transactional` Annotation이 특정 Class 혹은 Method에 '하나라도' 있으면,
 *   Transaction AOP는 `Proxy`를 만들어서 Spring Container에 등록한다.
 *
 *   => 따라서,
 *      실제  `basicService` 객체 대신, Proxy인 `basicService$$CGLIB`를 Spring Bean에 등록한다.
 *      그리고 Proxy는 내부에 실제 `basicService`를 참조한다.
 *
 *   => 핵심
 *      : 실제 객체가 아닌, Proxy 객체가 Spring Container에 Bean으로 등록된다!!
 * </pre>
 */
@Slf4j
@SpringBootTest
public class TransactionBasicTest {


  @Autowired
  BasicService basicService;

  @DisplayName("Transaction Proxy 작동여부 TEST")
  @Test
  void txProxyCheck() {
    log.info("AOP class ={} ", basicService.getClass());
    boolean isTxApplied = AopUtils.isAopProxy(basicService);
    assertThat(isTxApplied).isTrue(); // 현재 Thread에 Transaction 적용여부 확인한다. => Transaction 적용 여부를 가장 확실하게 확인할 수 있다.
  }


  @DisplayName("transactionTest")
  @Test
  void  txTest() {
    basicService.tx();
    basicService.nonTx();
  }

  @TestConfiguration
  static class TxApplyBasicConfig {
    @Bean
    BasicService basicService() {
      return new BasicService();
    }
  }

  @Slf4j
  public static class BasicService {
    @Transactional
    public void tx() {

      log.info("Call tX");
      boolean isTxActive = TransactionSynchronizationManager.isActualTransactionActive();
      log.info("TX active = {}", isTxActive); // result: transaction active = true

      /**
       * 결과
       * o.s.t.i.TransactionInterceptor           : Getting transaction for [hello.springtx.apply.TransactionBasicTest$BasicService.tx]
       * h.s.a.TransactionBasicTest$BasicService  : Call tX
       * h.s.a.TransactionBasicTest$BasicService  : TX active = true
       * o.s.t.i.TransactionInterceptor           : Completing transaction for [hello.springtx.apply.TransactionBasicTest$BasicService.tx]
       */

    }

    /**
     * <pre>
     *  nonTx 에서는 `@Transactional` Annotation이 없으므로, Transaction 적용 대상이 아니다.
     *  따라서,
     *    Transaction을 시작하지 않고, `basicService.nonTx()`를 호출하고, 종료한다. (Transaction 종료할 게 없다.)
     * </pre>
     */
    public void nonTx() {
      log.info("Call non tx");
      boolean isTxActive = TransactionSynchronizationManager.isActualTransactionActive();
      log.info("TX active = {}", isTxActive); // result: transaction active = false

    }
  }
}
