package core.spring.lifecycle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LifeCycleConfig {
  @Bean
  public NetworkClient setNetworkClient() {
    NetworkClient networkClient = new NetworkClient();
    networkClient.setUrl("http://hello.network.dev");
    return networkClient;
  }
}
