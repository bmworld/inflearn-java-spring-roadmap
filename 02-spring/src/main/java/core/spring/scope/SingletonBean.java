package core.spring.scope;

import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


/**
 * <h1>Spring scope - @Scope("singleton")</h1>
 * @- 기본값 'singleton' <br/>
 * @- This scope is the default value if no other scope is specified. <br/>
 * @- Spring Container 관리 대상임. => @PostConstruct, @PreDestroy 모두 적용됨 <br/>
 * <br/>
 * <br/>
 * <br/>
 * @See <a href="https://www.baeldung.com/spring-bean-scopes#singleton">https://www.baeldung.com/spring-bean-scopes#singleton</a>
 */
@Scope("singleton") // @Scope 기본 값: 'singleton'
public class SingletonBean {
  @PostConstruct
  public void init() {
    System.out.println("SingletonClientBean.init & this => " + this);
  }

  @PreDestroy
  public void destroy() {
    System.out.println("SingletonClientBean.destroy & this => " + this);
  }
}
