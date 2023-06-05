package core.spring.beanFinder;

import core.spring.config.AppConfig;
import core.spring.service.MemberService;
import core.spring.service.MemberServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * TEST 코드에는 실패 케이스도 반드시 포함하시라 (by 김영한 Sir)
 */
public class ApplicationContextBasicFindTest {

  private final AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

  @Test
  @DisplayName("빈 이름으로 조회")
  public void findBeanByName() throws Exception {
    MemberService memberService = ac.getBean("memberService", MemberService.class);
    assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
  }

  @Test
  @DisplayName("이름 없이 타입으로 조회 (인터페이스로 조회 시, 구현체가 출력된다.)")
  public void findBeanByType() throws Exception {
    MemberService memberService = ac.getBean(MemberService.class);
    assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
  }

  @Test
  @DisplayName("구현체의 타입으로 조회 (* 구현체 타입으로 조회 시, 유연성 떨어짐. Interface Bean으로 조회하시라.)")
  public void findBeanByInterfaceName() throws Exception {
    MemberService memberService = ac.getBean("memberService", MemberServiceImpl.class);
    assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
  }

  @Test
  @DisplayName("존재하지 않는 BEAN 이름으로 조회")
  public void findBeanByNameX() {
    assertThrows(NoSuchBeanDefinitionException.class, () -> ac.getBean("xxx", MemberServiceImpl.class));
  }


}
