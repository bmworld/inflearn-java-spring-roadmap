package core.spring.beanDefinition;

import core.spring.config.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

public class BeanDefinitionTest {
  AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
  GenericXmlApplicationContext acByXML = new GenericXmlApplicationContext("appConfig.xml");
  @Test
  @DisplayName("Bean 설정 Meta 정보 확인 -> @Configuration 어노테이션은 'Factory Method'를 사용한 Bean 등록 방식이다.")
  public void findApplicationBean() {
    String[] beanDefinitionNames = ac.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
      BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);
      if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
        System.out.println("--- beanDefinitionName = " + beanDefinitionName +
          "--- beanDefinition = " + beanDefinition
        );
      }
    }
  }

  @Test
  @DisplayName("Bean 설정 Meta 정보 확인 by XML ")
  public void findApplicationBeanInXML() {
    String[] beanDefinitionNames = acByXML.getBeanDefinitionNames();
    for (String beanDefinitionName : beanDefinitionNames) {
      BeanDefinition beanDefinition = acByXML.getBeanDefinition(beanDefinitionName);
      if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
        System.out.println("--- beanDefinitionName = " + beanDefinitionName +
          "--- beanDefinition = " + beanDefinition
        );
      }
    }

  }
}
