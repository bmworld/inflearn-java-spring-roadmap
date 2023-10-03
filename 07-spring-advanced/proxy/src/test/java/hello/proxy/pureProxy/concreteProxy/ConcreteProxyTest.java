package hello.proxy.pureProxy.concreteProxy;

import hello.proxy.pureProxy.concreteProxy.code.ConcreteClient;
import hello.proxy.pureProxy.concreteProxy.code.ConcreteLogic;
import hello.proxy.pureProxy.concreteProxy.code.TimeProxy;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class ConcreteProxyTest {

  @DisplayName("no Proxy")
  @Test
  void noProxy () {
    ConcreteLogic logic = new ConcreteLogic();
    ConcreteClient client = new ConcreteClient(logic);
    client.execute();
  }


  @DisplayName("proxy: 구체 Class 적용 (Interface 가 없어도 자바의 다형성으로 인해, 상위 타입만 맞으면, 다형성이 적용됨(")
  @Test
  void proxy () {
    ConcreteLogic logic = new ConcreteLogic();
    TimeProxy timeProxy = new TimeProxy(logic);
    ConcreteClient client = new ConcreteClient(timeProxy);
    client.execute();
  }
}
