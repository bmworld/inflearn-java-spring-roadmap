package core.spring.beanFinder;

import core.spring.config.AppConfig;
import core.spring.repository.MemberRepository;
import core.spring.repository.MemoryMemberRepository;
import core.spring.service.discount.DiscountPolicy;
import core.spring.service.discount.FixDiscountPolicy;
import core.spring.service.discount.RateDiscountPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextExtendsFindTest {
  private final AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

  @Test
  @DisplayName("부모 타입으로 조회 시, 자식이 둘 이상 있으면, 중복 오류가 발생")
  public void findBeanByParentTypeDuplicate() {
    assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(DiscountPolicy.class));
  }

  @Test
  @DisplayName("부모 타입으로 조회 시 , 자식이 둘 이상 있는 경우, 빈 이름을 지정한다")
  public void findBeanByParentTypeAndBeanName() {
    DiscountPolicy bean = ac.getBean("rateDiscountPolicy", DiscountPolicy.class);
    assertThat(bean).isInstanceOf(RateDiscountPolicy.class);

    DiscountPolicy bean2 = ac.getBean("fixDiscountPolicy", DiscountPolicy.class);
    assertThat(bean2).isInstanceOf(FixDiscountPolicy.class);
  }

  @Test
  @DisplayName("부모 타입으로 조회 시 , 특정 하위 타입으로 조회(자식 타입을 지정하는 것은 그다지 좋은 방법은 아니다. 왜냐? 유연성이 떨어짐)")
  public void findBeanBySubType() {
    DiscountPolicy bean = ac.getBean(RateDiscountPolicy.class);
    assertThat(bean).isInstanceOf(RateDiscountPolicy.class);
  }

  @Test
  @DisplayName("부모 타입으로 자식 빈 모두 조회")
  public void findAllBeanByParentType() {
    Map<String, DiscountPolicy> beansOfType = ac.getBeansOfType(DiscountPolicy.class);
    assertThat(beansOfType.size()).isEqualTo(2);
    for (String key : beansOfType.keySet()) {
      DiscountPolicy value = beansOfType.get(key);
      assertThat(value).isInstanceOf(DiscountPolicy.class);
      System.out.println(" --- key = " + key + " / value = " + value );
    }
  }

  @Test
  @DisplayName("최상위 부모(Object) 타입으로 모든 Bean 조회하기 (**Spring에 등록된 모든 객체가 조회된다. 모든 객체는 하나도 빠짐없이 Object Type이기 때문)")
  public void findAllByObjectType() {
    // Given
    Map<String, Object> beansOfType = ac.getBeansOfType(Object.class);
    for (String key : beansOfType.keySet()) {
      Object value = beansOfType.get(key);
      System.out.println(" --- key = " + key + " / value = " + value );
    }
    // When

    // Then

  }
  @Configuration
  static class TestConfig{
    @Bean
    public DiscountPolicy rateDiscountPolicy() {
      return new RateDiscountPolicy();
    }

    @Bean
    DiscountPolicy fixDiscountPolicy() {
      return new FixDiscountPolicy();
    }
  }



}
