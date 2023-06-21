package core.spring.scan.filter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComponentFilterAppConfigTest {
  @Test
  @DisplayName("@Configuration Filter 기능 테스트")
  public void filterScan() throws Exception {
    //Given
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);

    // When
    IncludeBean includeBean = ac.getBean("includeBean", IncludeBean.class);
    // Then
    assertThat(includeBean).isNotNull();


    // When / Then
    assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("excludeBean", ExcludeBean.class));

  }


  @Configuration
  @ComponentScan(
    includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyIncludeComponent.class),
    excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyExcludeComponent.class)
  )
  public static class ComponentFilterAppConfig {

  }
}
