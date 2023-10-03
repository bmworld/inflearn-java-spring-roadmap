package hello.proxy.pureProxy.concreteProxy.code;

import lombok.extern.slf4j.Slf4j;


/**
 * <h1>구체 Class를 상속받아, Proxy를 적용해보자</h1>
 * <pre>
 *   Proxy FLOW
 *   : client Request -> Proxy -> 구체 Class
 *
 *   - 구체 Class의 Origin Method를 `@Override` 하는 것이 포인트.
 * </pre>
 * <h1>핵심: 실제 사용할 ConcreteClient에 `timeProxy`를 주입하는 것 (구체클래스인 `ConcreteLogic` 주입 X)</h1>
 */
@Slf4j
public class TimeProxy extends ConcreteLogic {
  private ConcreteLogic concreteLogic;

  public TimeProxy(ConcreteLogic concreteLogic) {
    this.concreteLogic = concreteLogic;
  }

  @Override
  public String operation() {
    log.info("[@Override] Operate TimeProxy");
    long startTime = System.currentTimeMillis();
    String result = concreteLogic.operation();
    long endTime = System.currentTimeMillis();
    long workingTime = endTime - startTime;
    log.info("TimeDecorator 종료 > workingTime = {}ms", workingTime);
    return result;
  }
}
