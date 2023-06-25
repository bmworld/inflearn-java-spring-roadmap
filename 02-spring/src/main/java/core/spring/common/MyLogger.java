package core.spring.common;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;


/**
 * <h1>Web Scope -> Log 출력용 Class</h1>
 * @- Request는 HTTP 요청 당, 하나씩 생성되고, HTTP 요청이 끝나는 시점에 소멸된다
 * @- 이 빈이 생성됟는 시점에 자동으로 `@PostConstruct` 초기화 메서드 사용해서 uuid 생성 후 , 저장
 *    => HTTP 요청 당, 하나씩 생성되므로, uuid를 통하여, HTTP각 요청을 구분할 수 있게 된다.
 * @- 해당 Bean 소멸 시점에 `@PreDestroy`를 사용하여, 종료 메시지를 남긴다.
 * @- `requestURL`: Bean 생성 시점에는 알 수 없으므로, Setter 사용하여 외부에서 입력받음
 *
 */
@Component
@Scope(value="request") // ! 해당 Bean은 HTTP 요청이 오기 전까지는 생성되지 않음 => Provider를 사용해야만 하는 CASE.
public class MyLogger {

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
