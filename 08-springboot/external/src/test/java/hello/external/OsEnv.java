package hello.external;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OsEnv {
  public static void main(String[] args) {
    // 시스템 환경변수 모두 읽기
    Map<String, String> getenv = System.getenv();
    for (String key : getenv.keySet()) {

      String val = System.getenv(key);
      log.info("env {}={}", key, val);
    }

    // DB URL = dev.db.com
    // DB URL = prod.db.com
    // 각각 환경에 따라 서로 다른 값을 읽어들이게 한다.
    // 단, OS Scope 이므로, 변수명에 유의하시라.
    System.getenv("DB_URL");
  }
}
