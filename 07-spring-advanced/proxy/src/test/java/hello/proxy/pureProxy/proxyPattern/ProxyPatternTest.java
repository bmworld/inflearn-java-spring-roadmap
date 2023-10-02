package hello.proxy.pureProxy.proxyPattern;

import hello.proxy.pureProxy.proxyPattern.code.CacheProxy;
import hello.proxy.pureProxy.proxyPattern.code.ProxyPatternClient;
import hello.proxy.pureProxy.proxyPattern.code.RealSubject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProxyPatternTest {
  @Test
  @DisplayName("No proxy ver.")
  public void noProxyTest() {
    RealSubject realSubject = new RealSubject();
    ProxyPatternClient client = new ProxyPatternClient(realSubject);
    client.execute();
    client.execute();
    client.execute();
    /**
     * 결과
     * 19:09:49.625 [main] INFO hello.proxy.pureProxy.proxyPattern.code.RealSubject - 실제 객체 호출
     * 19:09:50.668 [main] INFO hello.proxy.pureProxy.proxyPattern.code.RealSubject - 실제 객체 호출
     * 19:09:51.674 [main] INFO hello.proxy.pureProxy.proxyPattern.code.RealSubject - 실제 객체 호출
     */
  }

  @Test
  @DisplayName("Proxy ver.")
  public void proxyWithAccessControl() {
    RealSubject realSubject = new RealSubject();
    CacheProxy proxy = new CacheProxy(realSubject);
    ProxyPatternClient client = new ProxyPatternClient(proxy);
    client.execute();
    client.execute();
    client.execute();

    /**
     * 결과
     * 19:10:11.829 [main] INFO hello.proxy.pureProxy.proxyPattern.code.CacheProxy - --- Call Proxy
     * 19:10:11.835 [main] INFO hello.proxy.pureProxy.proxyPattern.code.RealSubject - 실제 객체 호출
     * 19:10:12.844 [main] INFO hello.proxy.pureProxy.proxyPattern.code.CacheProxy - --- Call Proxy
     * 19:10:12.844 [main] INFO hello.proxy.pureProxy.proxyPattern.code.CacheProxy - --- Call Proxy
     */
  }
}
