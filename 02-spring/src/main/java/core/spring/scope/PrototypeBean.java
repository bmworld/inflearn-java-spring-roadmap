package core.spring.scope;

import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * <h1>Spring scope - @Scope("prototype") </h1>
 * @- Spring container는 prototype에 대한 요청이 존재할 때마다 '신규 생성'된다.
 * @- Spring conatiner는 prototype scope Bean의 생성과 의존관계 주입, 초기화까지만 관여
 * @- Spring conatiner에의해 '종료 메서드'가 호출되지 않는다. => `@PreDestroy` 미적용
 * @- A bean with the prototype scope will return a different instance every time it is requested from the container. It is defined by setting the value prototype to the @Scope annotation in the bean definition:
 * <br/>
 * <br/>
 * <br/>
 * @See <a href="https://www.baeldung.com/spring-bean-scopes#prototype">https://www.baeldung.com/spring-bean-scopes#prototype</a>
 */
@Scope("prototype") // @Scope 기본 값: 'singleton'
public class PrototypeBean {
  public int count =0;

  public void addCount() {
    count++;
  }

  public int getCount() {
    return count;
  }

  @PostConstruct
  public void init() {
    System.out.println("PrototypeBean.init & this => " + this);

  }

  @PreDestroy
  public void destroy() {

    System.out.println("PrototypeBean.destroy > 호출되지 않는 method");
  }
}
