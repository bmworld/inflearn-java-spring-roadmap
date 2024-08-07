package hello;

import hello.config.MyDataSourceConfigV3;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

// @Import(MyDataSourceEnvConfig.class)
// @Import(MyDataSourceValueConfig.class)
// @Import(MyDataSourceConfigV1.class)
// @Import(MyDataSourceConfigV2.class)
@Import(MyDataSourceConfigV3.class)
@ConfigurationPropertiesScan // @ConfigurationProperties 설정된 @Bean 을 모두 scan 해서 가져옴
@SpringBootApplication(scanBasePackages = {"hello.datasource", "hello.pay"})
public class ExternalReadApplication {

  public static void main(String[] args) {
    SpringApplication.run(ExternalReadApplication.class, args);
  }
}
