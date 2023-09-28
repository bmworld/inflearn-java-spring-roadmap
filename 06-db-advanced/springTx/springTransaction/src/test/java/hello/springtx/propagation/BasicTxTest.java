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
  public void commit() {

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
  public void rollback() {

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


  @Test
  @DisplayName("CASE: double commit:  `connection Pool` 사용하므로, 2개의 커넥션은 동일한 Con 사용하지만, 반납 후, 새로 사용한 것.")
  public void twoConnection_commitEach() {

    // connection pool => get connection => proxyConnection 생성 후, 반환
    // ex. HikariProxyConnection@995585763 wrapping conn0
    log.info("--- Starting tx 1");
    TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());

    log.info("--- Starting tx 1: Commit");
    txManager.commit(tx1);



    // =================================================================
    // connection pool => get connection => proxyConnection 생성 후, 반환
    // ex. HikariProxyConnection@1682999176 wrapping conn0
    log.info("--- Starting tx 2");
    TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());

    log.info("--- Starting tx 2: Commit");
    txManager.commit(tx2);


    log.info("--- Completed Transaction");

  }




  @Test
  @DisplayName("CASE: double commit:  `connection Pool` 사용하므로, 2개의 커넥션은 동일한 Con 사용하지만, 반납 후, 새로 사용한 것.")
  public void twoDifferenceConnection_commitAndRollback() {

    // connection pool => get connection => proxyConnection 생성 후, 반환
    // ex. HikariProxyConnection@995585763 wrapping conn0
    log.info("--- Starting tx 1");
    TransactionStatus tx1 = txManager.getTransaction(new DefaultTransactionAttribute());

    log.info("--- Starting tx 1: Commit");
    txManager.commit(tx1);



    // =================================================================
    // connection pool => get connection => proxyConnection 생성 후, 반환
    // ex. HikariProxyConnection@1682999176 wrapping conn0
    log.info("--- Starting tx 2");
    TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());

    log.info("--- Starting tx 2: Rollback");
    txManager.rollback(tx2);


    log.info("--- Completed Transaction");

  }


  /**
   * <h1>Outer Tx & Inner Tx 개념 설명</h1>
   * <pre>
   *   - 중심개념: 두 Tx는 하나의 물리적 Tx 내에 존재하는 논리적 Tx을 의미한다.
   *   - Outer: 하나의 물리적 Tx 내에서 '먼저' 실행된 논리적 Tx 을 의미.
   *     => isNewTransaction = true
   *   - Inner: 하나의 물리 Tx 내에서 `기존 Tx(Outer)` '이후' 실행된 논리적 Tx 을 의미.
   *     => isNewTransaction = false
   *
   * </pre>
   * <pre>
   *   Transaction 참여
   *   - 의미 : 외부 Tx오 내부 Tx가 하나의 물리 Tx로 묶인다.
   *   - Inner Tx가 외부 Tx를 그대로 이어 받는다.
   *   - 다른 관점으로 보면, 외부 Tx 범위가 내부 Tx까지 미친다는 의미.
   *   - 외부에서 시작된 물리적 Tx 범위가 내부 Tx까지 이어진다.
   * </pre>
   */
  @Test
  @DisplayName("CASE: inner commit - 동일한 물리적 Connection 내에서 2개의 Connection 사용 => 내부 Tx는 외부 Tx에 항상 참여")
  public void twoInnerConnection_asInnerConnection() {

    log.info("--- 외부 Tx 시작");
    TransactionStatus outer = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("--- outer.isNewTransaction() ={}", outer.isNewTransaction());



    // =================================================================================================================
    // 핵심: 내부 Tx는 외부 Tx에 참여한다.
    log.info("--- 내부 Tx 시작 =====> 먼저 시작된 Tx(outer)에 내부 Tx(inner)가 참여한다.");
    // Participating in existing transaction
    TransactionStatus inner = txManager.getTransaction(new DefaultTransactionAttribute());
    log.info("--- inner.isNewTransaction() ={}", inner.isNewTransaction()); // false => 내부 Tx 는 신규 Tx가 아.니.다 (외부 tx에 참여.)
    log.info("---- 내부 Tx Commit (**이때는, 외부 Tx에 참여중이므로, 내부 Tx 의 Commit은 아무 효력이 없다.)");
    txManager.commit(inner);
    // =================================================================================================================


    log.info("--- 외부 Tx Commit");
    txManager.commit(outer);



    // =================================================================
    // connection pool => get connection => proxyConnection 생성 후, 반환
    // ex. HikariProxyConnection@1682999176 wrapping conn0
    log.info("--- Starting tx 2");
    TransactionStatus tx2 = txManager.getTransaction(new DefaultTransactionAttribute());

    log.info("--- Starting tx 2: Rollback");
    txManager.rollback(tx2);


    log.info("--- Completed Transaction");

  }


  /**
   * <h1>물리 Transaction & 논리 Transaction </h1>
   * <pre>
   *   - 물리 Tx: 실제 DB 적용되는 Tx
   *     (`setAutoCommit(false)` 적용 후, 실제 Connection 을 통하여 Commit or Rollback 하는 단위.
   *   - 논리 Tx: 하나의 물리 Tx으로 묶인다.
   *
   *   - 핵심:  Spring은, 다수의 Tx가 함께 사용될 경우 (하나의 '물리 Tx' 내에 다른 '논리적' TX가 추가된 경우,
   *     처음 Tx를 시작한 외부 Tx이 실제 물리 Tx를 관리한다.
   *      -> 내부 tx에서 실행하는 Commit, rollback은 실효성이 없다.
   *      -> 즉, 외부 tx가 commit 또는 rollback 할 경우, 실제 적용된다.
   * </pre>
   * <h1>논리 Transaction 원칙</h1>
   * <pre>
   *   1. 모든 논리 Tx Commit 되어야, 물리 Tx Commit 된다.
   *   2. 하나의 논리 Tx라도 Rollback 될 경우, 물리 Tx는 Rollback 된다.
   * </pre>
   */

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
