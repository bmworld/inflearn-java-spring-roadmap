package core.spring.lifecycle;


public class NetworkClient_ByBeanMethodName {
  private String url;

  public NetworkClient_ByBeanMethodName() {
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


  public void init(){
    System.out.println("NetworkClient_ByBeanMethodName.init");
    connect();
    call("초기화 연결 메시지");
  }

  public void close() {
    System.out.println("NetworkClient_ByBeanMethodName.close");
    disconnect();
  }

}
