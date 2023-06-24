package core.spring.scope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;


/**
 * <h1>javax - {@link javax.inject.Provider} : ApplicationContext 조회</h1>
 * @- DL(Dependency Lookup) 기능만 제공 (필요한 만큼만 쓸 수 있다)
 * @- `get` method 하나로, 기능이 매우 단순
 * @- java 표준이므로 Spring 의존성 없이, 다른 Container에서도 사용가능함.
 * @- BUT 별도의 라이브러리 필요
 * @- 강사님의 추천 -> spring과 java 표준이 겹칠 때는, spring 제공기능을 쓰시라<br/>
 * but, spring에서 java 표준을 권장할 때는, java 표준 사용
 *
 */
@Scope("singleton")
public class SingletonWithPrototypeBeanAndJavaxProvider {

  @Autowired
  private Provider<PrototypeBean> contextProvider;


  public Map<String, Object> intentionalLogic() {
    PrototypeBean intendedPrototypeBean = contextProvider.get();
    intendedPrototypeBean.addCount();
    int count = intendedPrototypeBean.getCount();
    Map<String, Object> resultMap = new HashMap<>();
    resultMap.put("count", count);
    resultMap.put("bean", intendedPrototypeBean);
    return resultMap;
  }


  @PostConstruct
  public void init() {
    System.out.println("SingletonWithPrototypeBeanAndJavaxInjectProvider.init");
  }

  @PreDestroy
  public void destroy() {
    System.out.println("SingletonWithPrototypeBeanAndJavaxInjectProvider.init");
  }
}
