package hello.config;

import hello.datasource.MyDataSource;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MyDataSourceValueConfig {

  @Value("${my.datasource.url}")
  private String url;

  @Value("${my.datasource.username}")
  private String username;

  @Value("${my.datasource.password}")
  private String password;

  @Value("${my.datasource.max-connection}")
  private int maxConnection;

  @Value("${my.datasource.etc.timeout}")
  private Duration timout;

  @Value("${my.datasource.etc.options}")
  private List<String> options;

  @Bean
  public MyDataSource myDataSource() {
    return new MyDataSource(url, username, password, maxConnection, timout, options);
  }

  /** Param 방식 주입받기 */
  @Bean
  public MyDataSource myDataSource2(
      @Value("${my.datasource.url}") String url,
      @Value("${my.datasource.username2:defaultName}")
          String username, // 값이 없는 경우, Default 값을 할당할 수 있다.
      @Value("${my.datasource.password}") String password,
      @Value("${my.datasource.max-connection}") int maxConnection,
      @Value("${my.datasource.etc.timeout}") Duration timout,
      @Value("${my.datasource.etc.options}") List<String> options) {
    return new MyDataSource(url, username, password, maxConnection, timout, options);
  }
}
