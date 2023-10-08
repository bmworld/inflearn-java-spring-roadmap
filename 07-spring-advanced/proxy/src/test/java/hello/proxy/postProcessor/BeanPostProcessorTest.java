package hello.proxy.postProcessor;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



/**
 * <h1>Bean Post Processor</h1>
 * <pre>
 *
 * - `@PostConstruct`는 Bean 생성 이후에 Bean 초기화 역할을 한다.
 *   ==> 즉, 생성된 Bean을 한번 '조작'하는 것이다.
 * - Spring 은 `CommonAnnotationBeanPostProcessor`라는 Bean 후처리기를 자동등록하는데,
 *   여기서 @PostConstruct Annotation 붙은 메서드를 호출한다.
 *   -> 따라서, Spring 또한 Spring 내부기능 확장을 위해, Bean 후처리기를 사용함을 알 수 있다.
 * </pre>
 *
 */
public class BeanPostProcessorTest {
  @Test
  @DisplayName(" `BeanPostProcessor`를 사용하여 A Bean 등록 시, B로 바꿔치기 => A 는 Bean 등록되지 않음")
  void beanPostProcessorTest() {
    AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class); // Spring Bean 등록


    // A는 Bean 등록 후, `BeanPostProcessor`로 인해서, B로 바꿔치기됨
    B beanB = appContext.getBean("beanA", B.class);
    beanB.helloB();
    Assertions.assertThat(beanB).isInstanceOf(B.class);


    // A는 Bean으로 등록되지 않음
    Assertions.assertThatThrownBy(() -> appContext.getBean(A.class)).isInstanceOf(NoSuchBeanDefinitionException.class);

  }

  @Slf4j
  static class AtoBPostProcessor implements BeanPostProcessor {

    // Bean 생성 이후, 실행되는 Processor
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
      log.info("BeanName={} bean={}", beanName, bean);
      if (bean instanceof A) {
        log.info("------예제) BEAN A -> BEAN B 바꿔치기 됨");
        return new B();
      }

      return bean;
    }
  }

  @Slf4j
  @Configuration
  static class BeanPostProcessorConfig {
    @Bean(name = "beanA")
    public A a() {
      return new A();
    }

    @Bean
    public AtoBPostProcessor helloPostProcessor() {
      return new AtoBPostProcessor();
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
