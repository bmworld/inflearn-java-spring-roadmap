package hello.jdbc.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;



@Slf4j
public class ConnectionTest {

  @Test
  @DisplayName("driverManager")
  public void driverManager() throws Exception {
    // Given
    Connection con1 = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    Connection con2 = DriverManager.getConnection(URL, USERNAME, PASSWORD);

    log.info("connection1={}, class={} ", con1, con1.getClass());
    log.info("connection2={}, class={} ", con2, con2.getClass());

    // Then
    Assertions.assertThat(con1).isNotEqualTo(con2);
  }


  @Test
  @DisplayName("dataSourceDriverManager")
  public void dataSourceByDriverManager() throws Exception {
    // Given
    // DriverManagerDataSource : 항상 새로운 커넥션 획득
    DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    userDataSource(dataSource);
  }


  @Test
  @DisplayName("dataSourceByConnectionPool")
  public void dataSourceByConnectionPool() throws Exception {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(URL);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setMaximumPoolSize(10);
    dataSource.setPoolName("MyPool");
    userDataSource(dataSource);

    Thread.sleep(1000); // Thread Sleep 필요. WHY ? Thread Pool 내에 Connection 연결 시,ㅣ log 볼 수 있움.
  }


  /**
   * <h1>DataSource VS DriverManager</h1>
   * <pre>
   *   `DriverManager`: Connection 획득 시마다, `URL`, `USERNAME`, `PASSWORD`를 전달해야 한다.
   *   `DataSource`: 최초 객체 생성 시에만 ID, PW 입력, Connection 획득 시에서는 getConnection() 만 호출하면 됨
   *    => 즉, `설정`과 `사용`을 분리함.
   * </pre>
   *
   * <pre>
   *   - Pool 사이즈 초과 시, wainting 상태로 넘어가고, 기다린다.
   * </pre>
   */
  private void userDataSource(DataSource dataSource) throws SQLException {
    // Interface (DataSource)를 통해서, DB Connecrtion 가져오기
    Connection con1 = dataSource.getConnection();
    Connection con2 = dataSource.getConnection();

    log.info("connection1={}, class={} ", con1, con1.getClass());
    log.info("connection2={}, class={} ", con2, con2.getClass());
  }
}
