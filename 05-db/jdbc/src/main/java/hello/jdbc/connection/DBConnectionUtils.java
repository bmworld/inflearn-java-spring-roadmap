package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtils {



  /**
   * @DriverManager JDBC standard inteface 제공 connection {@linkplain DriverManager}
   * @Connection {@link Connection} -> H2 DB 사용 -> org.h2.jdbc.JdbcConnection 반환
   * @h2 {@link h2 Gradle:com.h2.database:h2.2.1.214 org.h2.Driver}
   */
  public static Connection getConnection(){

    try {
      Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
      // 결과
      // get connection = conn0: url=jdbc:h2:tcp://localhost/~/h2/spring-db user=SA,
      // class=class org.h2.jdbc.JdbcConnection
      log.info("--- get connection = {}, class={}", connection, connection.getClass());
      return connection;
    } catch (SQLException e) {
      throw new IllegalStateException(e);
    }
  }
}
