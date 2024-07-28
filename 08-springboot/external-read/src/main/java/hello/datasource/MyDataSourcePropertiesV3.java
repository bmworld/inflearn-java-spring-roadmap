package hello.datasource;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.time.Duration;
import java.util.List;
import lombok.Getter;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

/**
 * @Validated spring-boot 제공하는 Validation 사용
 */
@Getter
@Validated
@ConfigurationProperties("my.datasource")
public class MyDataSourcePropertiesV3 {
  @NotEmpty private String url;
  @NotEmpty private String username;
  @NotEmpty private String password;

  @Min(1)
  @Max(99)
  private int maxConnection;

  private Etc etc;

  public MyDataSourcePropertiesV3(
      String url, String username, String password, int maxConnection, @DefaultValue Etc etc) {
    this.url = url;
    this.username = username;
    this.password = password;
    this.maxConnection = maxConnection;
    this.etc = etc;
  }

  @Getter
  public static class Etc {

    @DurationMin(seconds = 1)
    @DurationMax(seconds = 60)
    private Duration timeout;

    private List<String> options;

    public Etc(Duration timeout, @DefaultValue("DEFAULT") List<String> options) {
      this.timeout = timeout;
      this.options = options;
    }
  }
}
