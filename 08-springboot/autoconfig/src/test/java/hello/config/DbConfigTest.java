package hello.config;

import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionManager;

@Slf4j
@SpringBootTest
class DbConfigTest {

  @Autowired
  private DataSource dataSource;
  @Autowired private TransactionManager transactionManager;

  @Autowired private JdbcTemplate jdbcTemplate;
  
  @Test
  @DisplayName("Springboot 자동주입대상 Bean 확인")
  public void checkBeanBySpringbootAutoConfiguration() throws Exception {

    // Given
    log.info("dataSource = {}", dataSource);
    log.info("transactionManager = {}", transactionManager);
    log.info("jdbcTemplate = {}", jdbcTemplate);
    
    // When / Then
    assertNotNull(dataSource);
    assertNotNull(transactionManager);
    assertNotNull(jdbcTemplate);


  }
}
