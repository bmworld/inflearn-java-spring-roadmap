package core.spring.lifecycle;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient_BySpringInterface implements InitializingBean, DisposableBean {
  private String url;

  public NetworkClient_BySpringInterface() {
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


  /**
   * 의존관계 주입이 끝난 경우 호출되는 메서드
   */
  @Override
  public void afterPropertiesSet() throws Exception {
    System.out.println("NetworkClient_BySpringInterface.afterPropertiesSet");
    connect();
    call("초기화 연결 메시지");
  }

  @Override
  public void destroy() throws Exception {
    System.out.println("NetworkClient_BySpringInterface.destroy");
    disconnect();
  }
}
