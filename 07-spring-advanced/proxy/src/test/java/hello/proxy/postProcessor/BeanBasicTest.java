package hello.proxy.postProcessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class BeanBasicTest {
  @Test
  void basicConfig () {
    AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(BasicConfig.class); // Spring Bean 등록


    // A는 Bean 등록
    A a = appContext.getBean("beanA", A.class);
    a.helloA();

    // B는 미등록
    Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> appContext.getBean(B.class));


  }

  @Slf4j
  @Configuration
  static class BasicConfig {
    @Bean(name = "beanA")
    public A a() {
      return new A();
    }
  }
  @Slf4j
  static class A {
    public void helloA() {
      log.info("Hello A");
    }
  }

  @Slf4j
  static class B {
    public void helloB() {
      log.info("Hello B");
    }
  }
}
