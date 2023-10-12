package hello.aop.exam.config;

import hello.aop.exam.aop.TraceAspect;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExamConfig {
  @Bean
  TraceAspect traceAspect() {
    return new TraceAspect();
  }
}
