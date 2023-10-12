package hello.aop;

import hello.aop.exam.config.ExamConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@Import(ExamConfig.class)
@SpringBootApplication
public class AopApplication {

  public static void main(String[] args) {
    SpringApplication.run(AopApplication.class, args);
  }

}
