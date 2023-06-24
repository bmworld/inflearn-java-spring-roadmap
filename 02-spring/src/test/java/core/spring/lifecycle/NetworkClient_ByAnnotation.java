package core.spring.lifecycle;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * <h1>Bean 시작, 종료 메서드 By Annotation </h1>
 * @장점 <br/>
 * - 최신 Spring에서 권장 <br/>
 * - Annotation 하나만 붙이면 되므로, 매우 편리 <br/>
 * - Spring에 비종송적 <br/>
 *   => Why? JSR250이라는 자바 표준이다.<br/>
 *   `javax.annotation.PostConstruct` (패키지를 보시라) <br/>
 *   - ComponentScan과 잘 어울린다. <br/>
 * <br/>
 * <br/>
 * @단점 외부 라이브러리에는 적용하지 못한다. <br/>
 *     => `외부 라이브러리` 초기화, 종료 시 @Bean 기능을 사용하시라.<br/>
 * <br/>
 */
public class NetworkClient_ByAnnotation {
  private String url;

  public NetworkClient_ByAnnotation() {
    System.out.println("생성자 호출, url = " + url);

  }

  public void setUrl(String url) {
    this.url = url;
  }

  // 서비스 시작 시 호출
  public void connect() {
    System.out.println("connect = " + url);
  }


  // 호출
  public void call(String message) {
    System.out.println("call = " + url + " / message = " + message);
  }

  // 서비스 종료 시 호출
  public void disconnect() {
    System.out.println("disconnect = " + url);
  }


  @PostConstruct
  public void init(){
    System.out.println("NetworkClient_ByAnnotation.init");
    connect();
    call("초기화 연결 메시지");
  }

  @PreDestroy
  public void close() {
    System.out.println("NetworkClient_ByAnnotation.close");
    disconnect();
  }

}
