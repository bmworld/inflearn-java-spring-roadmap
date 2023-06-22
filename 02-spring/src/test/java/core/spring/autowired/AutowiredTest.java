package core.spring.autowired;

import core.spring.DI.AutowiredAtField;
import core.spring.DI.AutowiredAtFieldNonTarget;
import core.spring.DI.AutowiredAtFieldTarget;
import core.spring.config.AutoAppConfig;
import core.spring.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AutowiredTest {


  @Test
  @DisplayName("@Autowired Field 생성자주입 테스트")
  public void autowiredTest() throws Exception {

    // Given: Spring Bean 등록여부 확인
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class);

    AutowiredAtFieldTarget autowiredAtFieldTarget = ac.getBean(AutowiredAtFieldTarget.class);
    AutowiredAtFieldNonTarget autowiredAtFieldNonTarget = ac.getBean(AutowiredAtFieldNonTarget.class);

    assertThat(autowiredAtFieldTarget).isNotNull();
    assertThat(autowiredAtFieldTarget).isInstanceOf(AutowiredAtFieldTarget.class);
    assertThat(autowiredAtFieldNonTarget).isNotNull();
    assertThat(autowiredAtFieldNonTarget).isInstanceOf(AutowiredAtFieldNonTarget.class);



    // When
    AutowiredAtField autowiredAtField = ac.getBean(AutowiredAtField.class);
    AutowiredAtFieldTarget target = autowiredAtField.getAutowiredAtFieldTarget();
    AutowiredAtFieldNonTarget nonTarget = autowiredAtField.getAutowiredAtFieldNonTarget();
    System.out.println("----- target = " + target);
    System.out.println("----- nonTarget = " + nonTarget);
    // Then
    assertThat(target).isInstanceOf(AutowiredAtFieldTarget.class);
    assertThat(nonTarget).isNull();

  }


  @Test
  @DisplayName("Autowired Option")
  public void AutowiredOption() throws Exception {
    // Given
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    System.out.println("ac = " + ac);

    // When

    // Then

  }


  /**
   * 주입받을 객체가 없는 3가지 케이스를 확인해본다.
   */
  private static class TestBean{

//    @Autowired(required = true)z

    @Autowired(required = false) // required =false 일 경우, 자동주입할 대상 ( 의존관계가 없으면 ), 수정자 Method 호출이 일어나지 않 는 다.
    public void noBean(Member noBean) {
      System.out.println("noBean = " + noBean); // 이 메서드 자체가, 호출되지 않음
    }


    @Autowired
    public void nullableBean(@Nullable Member nullable) { // @Nullable = 항상 메서드 호출이 일어나지만 DI 대상 없을 시, Null
      System.out.println("nullable = " + nullable); // => null
    }

    @Autowired
    public void optionalBean(Optional<Member> optional) { // Optional<Class> = 항상 메서드 호출이 일어나지만 DI 대상 없을 시, Optional
      System.out.println("optional = " + optional); // => Optional.empty

    }

  }

}
