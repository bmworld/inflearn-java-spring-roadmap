package hello.datasource;

import java.time.Duration;
import java.util.List;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * @DefaultValue 해당값을 찾을 수 없는 경우에, 기본값을 설정할 수 있다.
 */
@Getter
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV2 {
  private String url;
  private String username;
  private String password;
  private int maxConnection;
  private Etc etc;

  public MyDataSourcePropertiesV2(
      String url, String username, String password, int maxConnection, @DefaultValue Etc etc) {
    this.url = url;
    this.username = username;
    this.password = password;
    this.maxConnection = maxConnection;
    this.etc = etc;
  }

  @Getter
  public static class Etc {
    private Duration timeout;

    public Etc(Duration timeout, @DefaultValue("DEFAULT") List<String> options) {
      this.timeout = timeout;
      this.options = options;
    }

    private List<String> options;
  }
}
