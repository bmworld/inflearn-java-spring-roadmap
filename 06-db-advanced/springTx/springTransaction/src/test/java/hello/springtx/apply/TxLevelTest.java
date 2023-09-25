package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;


/**
 * <h1>Spring @Transactional Priority Rule > 항상 구체적인 것이 우선순위가 더 높다.</h1>
 * <pre>
 *   1. Class 적용 시, Method 에 자동적용
 *   2. Interface 에도 `@Transactional `적용 가능.
 *   3. 우선순위 규칙
 *     - (우선순위 높음) Class method > Class Type > Interface Method > interface Type (우선순위 낮음)
 *     - ex. method 내에서 Tx 시작 시, method 내에 `@Transactional` Annotation 없을 경우, Class Type을 찾고, 만약 없으면 더 상위 부모로 올라가서 찾는다.
 * </pre>
 */

@SpringBootTest
public class TxLevelTest {

  @Autowired
  LevelService service;

  @DisplayName("Fuction priority test")
  @Test void functionPriorityTest() {

    service.writableMethod();
    service.nonWritableMethod();
  }

  @TestConfiguration
  static class TxLevelTestConfig{

    @Bean
    LevelService levelService(){
      return new LevelService();
    }

  }


  @Slf4j
  @Transactional(readOnly = true)
  public static class LevelService {

    @Transactional(readOnly = false)
    public void writableMethod() {
      // 구체적인 level에 적용된 것이 , 우선순위가 더 높다. => Class 보다 Method Level이 더 구체적이다.
      log.info("Call write");
      prtTxInfo();
    }

    public void nonWritableMethod() {
      log.info("call read"); // Class level 에 적용된 Transactional Annotation이 적용되어, readOnly true가 그대로 적용된다.
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
