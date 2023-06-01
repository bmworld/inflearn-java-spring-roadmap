package core.spring.beanFinder;

import core.spring.config.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ApplicationContextTest {

  private final AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

  @Test
  @DisplayName("findAllApplicationContextBeans")
  public void finApplicationContextBeans() throws Exception{
    String[] beanDefinitionNames = ac.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
      Object bean = ac.getBean(beanDefinitionName);
      System.out.println("----- beanName = " + beanDefinitionName + " /  bean = " + bean);
    }
  }

  /**
   * @ROLE_APPLICATION 사용자(개발자)가 정의한 Bean (App 동작 시 사용되는 Bean)
   * @ROLE_INFRASTRUCTURE Spring Container 내부 사용 Bean
   * @ROLE_SUPPORT 복합 구조의 빈을 정의할 때 보조적으로 사용되는 빈의 역할을 지정하려고 정의 (실제로는 거의 사용되지 않음, 무시해도 괜찮음)
   */
  @Test
  @DisplayName("App bean 출력")
  public void findApplicationBeans() throws Exception{
    String[] beanDefinitionNames = ac.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
      BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

      if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
        Object bean = ac.getBean(beanDefinitionName);
        System.out.println("----- ROLE_APPLICATION > beanName = " + beanDefinitionName + " /  bean = " + bean);
      }

      if (beanDefinition.getRole() == BeanDefinition.ROLE_SUPPORT) {
        Object bean = ac.getBean(beanDefinitionName);
        System.out.println("----- ROLE_SUPPORT > beanName = " + beanDefinitionName + " /  bean = " + bean);
      }

      if (beanDefinition.getRole() == BeanDefinition.ROLE_INFRASTRUCTURE) {
        Object bean = ac.getBean(beanDefinitionName);
        System.out.println("----- ROLE_INFRASTRUCTURE > beanName = " + beanDefinitionName + " /  bean = " + bean);
      }

    }
  }
}
