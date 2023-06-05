package core.spring.beanFinder;

import core.spring.repository.MemberRepository;
import core.spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextSameBeanFindTest {
  private final AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SameBeanConfig.class); // 테스트를 위해서 만든 Config을 사용한다.

  @Test
  @DisplayName("Class Type으로 조회 시, 같은 Type이 2개 이상 조회될 경우,중복 오류 발생")
  public void findBeanByTypeDuplicate() {
    assertThrows(NoUniqueBeanDefinitionException.class, () -> ac.getBean(MemberRepository.class));

  }

  @Test
  @DisplayName("같은 타입이 둘 이상 있는 경우, 타입 외에 '빈 이름'을 지정하여 정상적으로 조회할 수 있다.")
  public void findBeanByName() {
    MemberRepository memberRepository = ac.getBean("memberRepository1", MemberRepository.class);
    MemberRepository memberRepository2 = ac.getBean("memberRepository2", MemberRepository.class);
    assertThat(memberRepository).isInstanceOf(MemberRepository.class);
    assertThat(memberRepository2).isInstanceOf(MemberRepository.class);
  }

  @Test
  @DisplayName("특정 타입의 빈을 모두 조회하기")
  public void findAllBeanByType() {
    Map<String, MemberRepository> beansOfType = ac.getBeansOfType(MemberRepository.class);
    for (String key : beansOfType.keySet()) {
      MemberRepository value = beansOfType.get(key);
      System.out.println("---- key = " + key + " / value = " + value);
    }
    assertThat(beansOfType.size()).isEqualTo(2);

  }
  @Configuration
  static class SameBeanConfig {
    @Bean
    public MemberRepository memberRepository1() {
      return new MemoryMemberRepository();

    }

    @Bean
    MemberRepository memberRepository2() {
      return new MemoryMemberRepository();
    }
  }


}
