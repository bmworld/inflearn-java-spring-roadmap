package hello;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaSystemPropertyUtils {
  public static void printSystemProps() {

    // JAVA 가 기본으로 들고있는 System Props
    Properties properties = System.getProperties();
    for (Object key : properties.keySet()) {
      String value = System.getProperty(String.valueOf((key)));

      log.info("[ prop ] {} = {}", key, value);
    }

    // =====
    String url = System.getProperty("url");
    String username = System.getProperty("username");
    String password = System.getProperty("password");

    log.info("url = {}", url);
    log.info("username = {}", username);
    log.info("password = {}", password);
  }
}
