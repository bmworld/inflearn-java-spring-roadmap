package hello.datasource;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV1 {
  private String url;
  private String username;
  private String password;
  private int maxConnection;
  private Etc etc = new Etc();

  @Data
  public static class Etc {
    private Duration timeout;
    private List<String> options = new ArrayList<>();
  }
}
