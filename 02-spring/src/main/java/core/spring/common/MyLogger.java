package core.spring.common;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;


// ###############################################################################
// ###############################################################################
// ###############################################################################
// #################################### 핵 심 #####################################
// ###############  !  "진짜 객체" 조회를 실제 필요한 시점까지 '지연' 한다는 것
// ###############################################################################
// ###############################################################################
// ###############################################################################



/**
 * <h1>Web Scope -> Log 출력용 Class</h1>
 * @- Request는 HTTP 요청 당, 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸된다
 * @- 이 빈이 생성됟는 시점에 자동으로 `@PostConstruct` 초기화 메서드 사용해서 uuid 생성 후 , 저장
 *    => HTTP 요청 당, 하나씩 생성되므로, uuid를 통하여, HTTP각 요청을 구분할 수 있게 된다.
 * @- 해당 Bean 소멸 시점에 `@PreDestroy`를 사용하여, 종료 메시지를 남긴다.
 * @- `requestURL`: Bean 생성 시점에는 알 수 없으므로, Setter 사용하여 외부에서 입력받음
 *
 * <br/>
 * <br/>
 * <br/>
 * <br/>
 * @- proxyMode: 가짜(proxy) Class를 만들어두고, HTTP Request와 관계없이 해당 클래스를 다른 Bean에 미리 주입할 수 있게 된다.
 * @- ScopedProxyMode.TARGET_CLASS: 적용 대상이 CLASS  (interface 아님)
 * @- ScopedProxyMode.INTERFACES: 적용 대상이 interface (Class 아님)
 */
@Component
@Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS) // ! 해당 Bean은 HTTP 요청이 오기 전까지는 생성되지 않음 => Provider를 사용해야만 하는 CASE.
public class MyLogger {
  /**
   * @proxy객체? `CGLIB` (Byte Codee 조작) 라이브러리를 사용하여, 해당 Class를 상속받은 Proxy Class 생성 후 주입한다.
   * @-      해당 Class를  확인 시, 순수한 MylogClass가 아님 <br/>
   *        e.g.) core.spring.common.MyLogger$$EnhancerBySpringCGLIB$$8b481f3f
   *        <br/><br/>
   * @- request scope Class는, 실제 HTTP 요청이 들어오고 난 뒤, proxy -> 실제 객체가 생성된다.
   * @- Proxy Class는 실제 요청이 들어오면, 그떄 내부에서 실제 Bean을 요청하는 `위임 로직`이 들어 있다.
   * @- Proxy Class는 실제 Request scope와 관계까 없다.<br/>
   *    그냥 가짜다. 내부에 단순한 `위임 로직`만 있고, Singleton과 같이 동작한다.
   * @- Proxy: 원본 Class를 상속 받아서 만들어졌기 때문에, 객체를 사용하는 Client는, 해당 객체가 원본인지 아닌지 구별이 안될만큼, 동일하게 사용할 수 있다.
   * @- => `다형성`, `DI scope`의 강점
   *
   * <br/>
   * <br/>
   * <br/>
   * <br/>
   *
   * @주의 singleton과 다르기떄문에, '주의'해서 사용해야함 <br/>
   *      이런 특별한 scope는 필요한 곳에 최소한으로 사용하시라. <br/>
   *      why? 무분별하게 사용 시, 유지보수 어려워짐
   *
   */
  private String uuid;
  private String requestURL;

  public void setRequestURL(String requestURL) {
    this.requestURL = requestURL;
  }

  public void log(String message) {
    System.out.println("[" + uuid + "] [ " + requestURL  + " ] " + message);
  }


  @PostConstruct
  public void init() {
    this.uuid =  UUID.randomUUID().toString(); // uuid 겹칠 확률은 로또의 로또의 로또의 당첨 확률 정도임
    System.out.println("[" + uuid + "] request scope bean create: " + this);
  }

  @PreDestroy
  public void close() {
    System.out.println("[" + uuid + "] request scope bean close: " + this);
  }
}
