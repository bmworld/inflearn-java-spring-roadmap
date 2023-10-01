package hello.advanced.config;

import hello.advanced.trace.logTrace.FieldLogTrace;
import hello.advanced.trace.logTrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// Singleton 으로 LogTrace를 Bean 등록하자.
@Configuration
public class LogTraceConfig {
  @Bean
  public LogTrace logTrace() {
    return new FieldLogTrace();
  }
}
