package core.spring.scope;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;


public class PrototypeBeanTest {

  @Test
  @DisplayName("Prototype Scope")
  public void prototypeScopeBean() throws Exception {
    // Given
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
    // When
    System.out.println("find prototypeBean1 => 호출되는 타이밍에 prototype Scope의 Bean의 init() 실행된다.");
    PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);


    System.out.println("find prototypeBean2 => 호출되는 타이밍에 prototype Scope의 Bean의 init() 실행된다.");
    PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);

    System.out.println("prototypeBean = " + prototypeBean);
    System.out.println("prototypeBean2 = " + prototypeBean2);
    assertThat(prototypeBean).isNotSameAs(prototypeBean2); // isSameAs: `==` 비교


    ac.close();
    System.out.println("----prototype Bean -> Destroy 적용되지 않음");
  }

}
