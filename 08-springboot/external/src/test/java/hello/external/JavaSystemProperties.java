package hello.external;

import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaSystemProperties {
  public static void main(String[] args) {

    // JAVA 가 기본으로 들고있는 System Props
    Properties properties = System.getProperties();
    for (Object key : properties.keySet()) {
      String value = System.getProperty(String.valueOf((key)));

      log.info("[ prop ] {} = {}", key, value);
    }

    // ========= 환경변수 주입한 것을 확인해보자.
    // e.g build 후, 환변경수 주입
    // > java -Durl=hello -Dusername=bm -Dpassword=123 -jar ./build/libs/external-0.0.1-SNAPSHOT.jar
    String url = System.getProperty("url");
    String username = System.getProperty("username");
    String password = System.getProperty("password");

    log.info("url = {}", url);
    log.info("username = {}", username);
    log.info("password = {}", password);

    // ============ Code 에서 환경변수 주입
    String customKey = "custom_key";
    System.setProperty(customKey, "custom_value");
    String custom_value = System.getProperty(customKey);
    log.info("custom_value = {}", custom_value);
  }
}
