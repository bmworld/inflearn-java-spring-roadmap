package hello.config;

import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.transaction.TransactionManager;

@Slf4j
@Configuration
public class DbConfig {

  /** DB 지정 및 계정 설정 */
  @Bean
  public DataSource dataSource() {
    log.info("--- Register dataSource Bean");
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setDriverClassName("org.h2.Driver");
    dataSource.setJdbcUrl("jdbc:h2:mem:test"); // memory 내에 DB올림
    dataSource.setUsername("sa");
    dataSource.setPassword("");

    return dataSource;
  }

  /** DB Transaction 관리 */
  @Bean
  TransactionManager transactionManager() {
    log.info("--- Register DbConfig.transactionManager Bean");
    return new JdbcTransactionManager(dataSource());
  }

  /** DB 관리 */
  @Bean
  public JdbcTemplate jdbcTemplate() {
    log.info("--- Register DbConfig.jdbcTemplate Bean");
    return new JdbcTemplate(dataSource());
  }
}
