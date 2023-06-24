package core.spring.lifecycle;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 *
 * <h1>Spring Bean - Life Cycle</h1>
 * `객체 생성` => `의존 관계 주입` <br/>
 * <br/>
 * <h1>Spring Bean - Event Life Cycle</h1>
 *
 * <pre>
 *   Container Started -> Bean Instantiated -> Dependencies Injected <br/>
 *   -> Custom init() method -> Custom util method -> Custom destroy() method
 *   -> Shutdown Spring Application
 * </pre>
 *
 * <h1>[참고] 객체 생성과 초기화를 분리하자 </h1>
 * <pre>
 *   - 생성자: 필수 정보(파라미터)를 받고, 메모리를 할당해서 객체를 생성하는 책임을 가진다.
 *
 *   - 초기화: 생성된 값을 활용해서외부 connection 을 연결하는 등 '무거운 동작'을 수행한다.
 *
 *   => 생성자 안에 무거운 작업을` 피하는 것은 `유지보수` 관점에서 좋다.
 *      따라서 객체를 생성하는 부분과 초기화하는 부분을 명확하게 나누시라.
 *   => 물론, 초기화 작업이 단순한 경우에는 생정자에서 한번에 처리할 수 있다.
 *      (ex. 내부 값들의 약간의 변경)
 * </pre>
 *
 * @see <a href="https://www.geeksforgeeks.org/bean-life-cycle-in-java-spring/">Bean Life cycle in Java Spring</a>
 *
 */
public class BeanLifeCycleTest {
  @Test
  @DisplayName("Bean Lifecycle Test")
  public void beanLifeCycle() {
    // Given
    ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);

    ac.getBean(NetworkClient_NewVersion.class);
    ac.close(); // ApplicationContext 종료시킨다.

  }


  // #################################################################
  // ##### @Configuration => InnerClass일 경우, `static` class로 생성하시라

  //  @Configuration
  //  static public class LifeCycleConfig {
  //    @Bean
  //    public NetworkClient setNetworkClient() {
  //      NetworkClient networkClient = new NetworkClient();
  //      networkClient.setUrl("http://hello.network.dev");
  //      return networkClient;
  //    }
  //  }
  // #################################################################

}
