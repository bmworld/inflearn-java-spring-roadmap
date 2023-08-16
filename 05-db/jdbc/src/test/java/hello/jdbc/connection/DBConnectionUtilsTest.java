package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
class DBConnectionUtilsTest {

  @Test
  void getConnection() {
    Connection connection = DBConnectionUtils.getConnection();
    assertThat(connection).isNotNull();
  }
}
