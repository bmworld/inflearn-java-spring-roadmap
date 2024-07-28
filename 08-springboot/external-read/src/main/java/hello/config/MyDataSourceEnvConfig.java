package hello.config;

import hello.datasource.MyDataSource;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class MyDataSourceEnvConfig {
  private final Environment env;

  public MyDataSourceEnvConfig(Environment env) {
    this.env = env;
  }

  @Bean
  public MyDataSource myDataSource() {

    String url = env.getProperty("my.datasource.url");
    String username = env.getProperty("my.datasource.username");
    String password = env.getProperty("my.datasource.password");
    int maxConnection = env.getProperty("my.datasource.max-connection", Integer.class);
    Duration timout = env.getProperty("my.datasource.etc.timeout", Duration.class);
    List<String> options = env.getProperty("my.datasource.etc.options", List.class);
    return new MyDataSource(url, username, password, maxConnection, timout, options);
  }
}
