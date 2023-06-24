package core.spring.lifecycle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <h1>Bean Life Cycle - `설정정보` 사용 특징</h1>
 * <pre>
 *   - 시작/종료 메서드 이름을 자유롭게 지정할 수 있음
 *   - Spring Bean이 Spring Code에 의존하지 않음
 *   -외부 라이브러리에도 초기화, 종료 메서드를 지정할 수 있음
 *    Why? Code가 아닌, `설정 정보`를 사용하기 때문
 * </pre>
 *
 * <h1>[주의] Bean Life Cycle - `설정정보` - `종료 메서드`의 추론 기능</h1>
 * <pre>
 *   - @Bean으로 등록한 destroyMethod의 아주 특별한 기능
 *   - @Bean의 destroyMethod는 Default 값이 `(inferred)`(=추론)으로 등록됨
 *   - 이 추론은 `close`, `shutdown` 이름을 가진 method를 자동으로 호출한다.
 *     즉, 종료메서드 이름을 `infer 추론`하여 호출하는 것이다
 *   - 따라서, @Bean으로 등록하고 종료 메서드 이름을 `close`, `shutdown`으로 지정한 경우,
 *     `@Bean(destroyMethod={}) 이름을 지정해주지 않아도 됨.
 *
 *   * 추론기능 비활성화 방법: @Bean(destroyMethod="") (이름에 공백 할당)
 *
 * </pre>
 * @see Bean#destroyMethod()
 */

@Configuration
public class LifeCycleConfig {

  /**
   * <h1>@Bean 메서드 이름 지정 방법</h1>
   * <pre>
   *   @적용방법: @Bean(initMethod = "초기화 메서드명", destroyMethod = "종료시킬 메서드명") <br/>
   *   => @Configuration의 생명주기에 따라서
   *    * initMethod, destroyMethod를 지정할 수 있다.
   * </pre>
   * <br/>
   * <br/>
   * <br/>
   * @initMethod 해당 메서드 초기화 시 실행시킬 method 이름 <br/>
   *             => NetworkClient_NewVersion > init() 실행
   * @destroyMethod 해당 메서드 소멸 시 실행시킬 method 이름 <br/>
   *             => NetworkClient_NewVersion > close() 실행 <br/>
   *             => AnnotationConfigApplicationContext가 close 될 경우, 실행됨
   */
//  @Bean(initMethod = "init", destroyMethod = "close")
  @Bean
  public NetworkClient_ByAnnotation setNetworkClient() {
    NetworkClient_ByAnnotation networkClient = new NetworkClient_ByAnnotation();
    networkClient.setUrl("http://hello.network.dev");
    return networkClient;
  }
}
