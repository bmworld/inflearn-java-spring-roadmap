package core.spring.scope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;



/**
 * <h1>Spring scope - 생성자 주입 시, 핵심</h1>
 * @- 주입받을 대상인 Bean => AnnotationConfigApplicationContext 함께 등록되어야 함.
 *<br/>
 * @-Ex new AnnotationConfigApplicationContext(SingletonWithPrototypeBean.class, PrototypeBean.class);
 */
@Scope("singleton")
public class SingletonWithPrototypeBean {


  private final PrototypeBean prototypeBean; // 생성 시점에, 주입



  // Constructor 시점에 Spring Container 에서 prototype Bean 요청 => Prototype Bean이 이 시점에 생성됨
  @Autowired
  public SingletonWithPrototypeBean(PrototypeBean prototypeBean) {
    this.prototypeBean = prototypeBean;
  }

  public int unintentionalLogic() {
    // Singleton에서 Prototype 로직의 의도와 달리,  `신규생성되지 않음`
    // 최초 주입받은 prototype 재사용된다.
    // 즉, prototype을 사용하는 의도와 맞지 않게됨
    prototypeBean.addCount();
    return prototypeBean.getCount();
  }


  public int intentionalLogicButBadCode(ApplicationContext ac) {
    // * 어떤 방식이든, ApplicationContext를 singleton에서 주입받는 것은, 좋은 설계가 아님.
    PrototypeBean ptBean = ac.getBean(PrototypeBean.class);
    ptBean.addCount();
    return ptBean.getCount();


  }

  @PostConstruct
  public void init() {
    System.out.println("SingletonClientBean.init & this => " + this);
  }

  @PreDestroy
  public void destroy() {
    System.out.println("SingletonClientBean.destroy & this => " + this);
  }
}
