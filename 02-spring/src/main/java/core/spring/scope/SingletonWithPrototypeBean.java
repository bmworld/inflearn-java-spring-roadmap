package core.spring.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.HashMap;
import java.util.Map;


/**
 * <h1>Spring scope - 생성자 주입 시, 핵심</h1>
 * @- 주입받을 대상인 Bean => AnnotationConfigApplicationContext 함께 등록되어야 함.
 *<br/>
 * @-Ex new AnnotationConfigApplicationContext(SingletonWithPrototypeBean.class, PrototypeBean.class);
 */
@Scope("singleton")
public class SingletonWithPrototypeBean {


  private final PrototypeBean prototypeBean; // 생성 시점에, 주입

  /**
   * ApplicationContext를 Singleton에서 주입받는 것은, 좋은 설계가 아님
   */
  @Autowired
  private ApplicationContext ac;



  /**
   * @ObjectFactory ApplicationContext 내부의 Bean의 `찾는 기능` 만 제공(기능 단순함) <br/>
   *  - 별도의 라이브러리 필요없음<br/>
   *  - Spring에 의존적<br/>
   *  - {@link ObjectFactory#getObject()}
   * @ObjectProvider {@link ObjectFactory}기능 + 추가적인 기능 <br/>
   *                  따라서, ApplicationContext 주입 없이 (의존관계를 끊고), Prototype을 의도대로 사용 가능 <br/>
   *
   *
   */
  @Autowired
  private ObjectProvider<PrototypeBean> prototypeBeanProvider;

  // Constructor 시점에 Spring Container 에서 prototype Bean 요청 => Prototype Bean이 이 시점에 생성됨
  @Autowired
  public SingletonWithPrototypeBean(PrototypeBean prototypeBean) {
    this.prototypeBean = prototypeBean;
  }

  public int intentionalLogic() {
    PrototypeBean intendedPrototypeBean = prototypeBeanProvider.getObject();
    intendedPrototypeBean.addCount();
    return intendedPrototypeBean.getCount();
  }

  public PrototypeBean getPrototypeBean() {
    return prototypeBean;
  }


  public PrototypeBean getPrototypeBeanByObjectProvider() {
    return prototypeBeanProvider.getObject();
  }
  public int unintentionalLogic() {
    // Singleton에서 Prototype 로직의 의도와 달리,  `신규생성되지 않음`
    // 최초 주입받은 prototype 재사용된다.
    // 즉, prototype을 사용하는 의도와 맞지 않게됨
    prototypeBean.addCount();
    return prototypeBean.getCount();
  }


  public Map<String, Object> intentionalLogicButBadCode() {

    PrototypeBean ptBean = ac.getBean(PrototypeBean.class);
    ptBean.addCount();
    int count = ptBean.getCount();
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("count", count);
    resultMap.put("bean", ptBean);
    return resultMap;
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
