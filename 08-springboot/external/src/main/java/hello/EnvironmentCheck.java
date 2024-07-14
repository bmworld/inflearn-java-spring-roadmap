package hello;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 *
 * <h1>[ Environment ] -> Spring 제공하는 환경변수 통합 값</h1>
 *
 * <pre>
 *  - Spring 은 로딩 시점에 필요한 PropertySource 들을 생성하고, Environment 에서 사용할 수 있게 연결해준다.
 *  - [ Environment ] 포함값
 *  1. Commandline Option Args (=> PropertySource 생성)
 *  2. Java System Properties (=> PropertySource 생성)
 *  3. OS Environment Variables (=> PropertySource 생성)
 *  4. Setting Data file (=> PropertySource 생성)
 *  </pre>
 */
@Slf4j
@Component
public class EnvironmentCheck {
  private final Environment env;

  public EnvironmentCheck(Environment env) {
    this.env = env;
  }

  @PostConstruct
  public void init() {
    System.out.println("--- EnvironmentCheck.init");
    String url = env.getProperty("url");

    String username = env.getProperty("username");
    String password = env.getProperty("password");
    log.info("url = {}", url);
    log.info("username = {}", username);
    log.info("password = {}", password);
  }
}
