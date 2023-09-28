package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import javax.sql.DataSource;

@SpringBootTest
@Slf4j
public class BasicTxTest {
  @Autowired
  private PlatformTransactionManager txManager; // 아래 Config에서, 직접 등록한 txManager 사용한다.



  @Test
  @DisplayName("CASE: commit")
  public void commit() throws Exception {

    log.info("--- Starting Transaction"); // tx 시작 시, connection pool 에서 Connection 가져옴
    TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionAttribute());

    log.info("--- Starting Transaction Commit"); // commit 후에는 JDBC connection 을 반환한다.
    txManager.commit(txStatus);

    log.info("--- Completed Transaction");

    /**
     * <RESULT>
     * --- Starting Transaction
     * Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
     * Acquired Connection [HikariProxyConnection@995585763 wrapping conn0: url=jdbc:h2:mem:24bdfa0a-ab4a-4818-b57c-7670baec3dd4 user=SA] for JDBC transaction
     * Switching JDBC Connection [HikariProxyConnection@995585763 wrapping conn0: url=jdbc:h2:mem:24bdfa0a-ab4a-4818-b57c-7670baec3dd4 user=SA] to manual commit
     * --- Starting Transaction Commit
     * Initiating transaction commit
     * Committing JDBC transaction on Connection [HikariProxyConnection@995585763 wrapping conn0: url=jdbc:h2:mem:24bdfa0a-ab4a-4818-b57c-7670baec3dd4 user=SA]
     * Releasing JDBC Connection [HikariProxyConnection@995585763 wrapping conn0: url=jdbc:h2:mem:24bdfa0a-ab4a-4818-b57c-7670baec3dd4 user=SA] after transaction
     * --- Completed Transaction
     *
     */

  }



  @Test
  @DisplayName("CASE: rollback")
  public void rollback() throws Exception {

    log.info("--- Starting Transaction"); // tx 시작 시, connection pool 에서 Connection 가져옴
    TransactionStatus txStatus = txManager.getTransaction(new DefaultTransactionAttribute());

    log.info("--- Starting Transaction Commit"); // rollback 후도 commit과 동일하게 JDBC connection 을 반환한다.
    txManager.rollback(txStatus);

    log.info("--- Completed Transaction");

    /**
     * <RESULT>
     * --- Starting Transaction
     * Creating new transaction with name [null]: PROPAGATION_REQUIRED,ISOLATION_DEFAULT
     * Acquired Connection [HikariProxyConnection@1885180239 wrapping conn0: url=jdbc:h2:mem:ac2308ec-223d-4a52-98bd-54d086eb5b30 user=SA] for JDBC transaction
     * Switching JDBC Connection [HikariProxyConnection@1885180239 wrapping conn0: url=jdbc:h2:mem:ac2308ec-223d-4a52-98bd-54d086eb5b30 user=SA] to manual commit
     * --- Starting Transaction Commit
     * Initiating transaction rollback
     * Rolling back JDBC transaction on Connection [HikariProxyConnection@1885180239 wrapping conn0: url=jdbc:h2:mem:ac2308ec-223d-4a52-98bd-54d086eb5b30 user=SA]
     * Releasing JDBC Connection [HikariProxyConnection@1885180239 wrapping conn0: url=jdbc:h2:mem:ac2308ec-223d-4a52-98bd-54d086eb5b30 user=SA] after transaction
     * --- Completed Transaction
     */

  }



  @TestConfiguration
  static class Config{
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
      // 직접 등록하기.
      // 원래는 Spring 에서 자동등록해
      return new DataSourceTransactionManager(dataSource);
    }
  }


}
