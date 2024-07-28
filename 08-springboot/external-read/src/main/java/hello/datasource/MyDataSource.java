package hello.datasource;

import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MyDataSource {
  private String url;
  private String username;
  private String password;
  private int maxConnection ;
  private Duration duration;
  private List<String> options;

  public MyDataSource(String url, String username, String password, int maxConnection,
      Duration duration, List<String> options) {
    this.url = url;
    this.username = username;
    this.password = password;
    this.maxConnection = maxConnection;
    this.duration = duration;
    this.options = options;
  }

  @PostConstruct
  public void init(){
    log.info("url = {}", url);
    log.info("username = {}", username);
    log.info("password = {}", password);
    log.info("maxConnection = {}", maxConnection);
    log.info("duration = {}", duration);
    log.info("options = {}", options);
}
}
