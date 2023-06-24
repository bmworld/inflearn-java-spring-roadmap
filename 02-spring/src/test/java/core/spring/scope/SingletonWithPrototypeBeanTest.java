package core.spring.scope;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h1>Spring scope -  singleton Bean과 prototype Bean을 함께 사용 시, 문제점 </h1>
 * @- Singleton Bean에서 prototype Bean을 사용하는 경우,
 * prototype Beand은 이미 과거에 주입이 끝난 빈이다.
 * 따라서, Spring Container Bean 주입 시점에, 주입된 prototype bean은 spring container에 의해 신규 생성된 이후, 신규 생성되지 않.는.다.`
 */
public class SingletonWithPrototypeBeanTest {
  @Test
  @DisplayName("Singleton Bean에 주입받은 Prototype - 의도대로 작동하지 않는 경우")
  public void unintentionalPrototypeInSingleton() {


    // Given
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
      SingletonWithPrototypeBean.class,
      PrototypeBean.class  // SingletonWithPrototypeBean은 PrototypeBean을 주입받기 때문에, Context 내에 포함시켜야 함
      // ...
      // SingleTon Bean에 주입받을 Bean 있다면, 추가하시라.
      // ...
    );

    PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
    int initCount = prototypeBean.getCount();
    SingletonWithPrototypeBean singleTonBean1 = ac.getBean(SingletonWithPrototypeBean.class);
    SingletonWithPrototypeBean singletonBean2 = ac.getBean(SingletonWithPrototypeBean.class);



    // When - 첫 번째 변경
    int count1 = singleTonBean1.unintentionalLogic();
    int intendedCount = initCount + 1 ;

    // Then
    assertThat(count1).isEqualTo(intendedCount);

    // When - 두 번째 변경
    int count2 = singletonBean2.unintentionalLogic();

    assertThat(count2).isNotEqualTo(intendedCount);



  }


  @Test
  @DisplayName("Singleton Bean에 주입받은 Prototype - 의도대로 작동하지만, 좋은 설계가 아닌 코드")
  public void intentionalPrototypeInSingleton_butBadArchitecture() {


    // Given
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(
      SingletonWithPrototypeBean.class,
      PrototypeBean.class
    );

    PrototypeBean prototypeBean = ac.getBean(PrototypeBean.class);
    int initCount = prototypeBean.getCount();
    SingletonWithPrototypeBean singleTonBean1 = ac.getBean(SingletonWithPrototypeBean.class);
    SingletonWithPrototypeBean singletonBean2 = ac.getBean(SingletonWithPrototypeBean.class);



    // When - 첫 번째 변경
    int count1 = singleTonBean1.intentionalLogicButBadCode(ac);
    int intendedCount = initCount + 1 ;

    // Then
    assertThat(count1).isEqualTo(intendedCount);

    // When - 두 번째 변경
    int count2 = singletonBean2.intentionalLogicButBadCode(ac);

    assertThat(count2).isEqualTo(intendedCount);




  }


  @Test
  @DisplayName("Prototype 단독으로 사용되는 경우")
  public void prototypeWithoutSingleton() throws Exception {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);


    // Bean 1
    PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
    System.out.println("prototypeBean1 = " + prototypeBean1);
    prototypeBean1.addCount();
    assertThat(prototypeBean1.getCount()).isEqualTo(1);




    // Bean 2
    PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
    System.out.println("prototypeBean2 = " + prototypeBean2);
    prototypeBean2.addCount();
    assertThat(prototypeBean2.getCount()).isEqualTo(1);



    // Then
    assertThat(prototypeBean1).isNotSameAs(prototypeBean2); // isSameAs: `==` 비교
    ac.close(); // ac.close() => prototype에는 spring container에서 종료시점을 관리하지 않으므로, 영향을 끼치지 않는다.
  }


}
