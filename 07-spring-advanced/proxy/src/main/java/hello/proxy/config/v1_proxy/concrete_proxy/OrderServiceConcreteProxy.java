package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

/**
 * <h1>구체 Class 기반 Proxy 단점: `super(null)`</h1>
 * <pre>
 *   - 자바 기본 문법에 의해 클래스는 생성 시, 항상 `super()`로 부모 클래스의 생성자를 호출해야 함.
 *     => 이 부분 생략 시, 기본 생성자 호출됨
 *
 *   - 그런데 부모 class는 기본 생성자가 없고, 생성자 파라미터 1개를 필수로 받는다.
 *     따라서, 해당 필수 파라미터를 넣어서, `super(..)` 호출해야한다.
 *
 *   - Proxy는 `부모 객체의 기능을 사용하지 않기에`, `super(null)` 입력해도 무방하다.
 *     => 본인이 이해한 바를정리하자면, Proxy 는 `부모 객체 자체를 주입받으므로`,
 *        자바 문법에 필수인 `super(...)` 문법을 반드시 써야하는 이 상황에서, 어쩔 수 없기 super(null)을 사용하는 것이다.
 *   - cf)
 *     interface 기반 Proxy는 이런 고민을 하지 않아도 된다.
 * </pre>
 */
public class OrderServiceConcreteProxy extends OrderServiceV2 {
  private final OrderServiceV2 target;
  private final LogTrace logTrace;

  public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
    // ======= 구체 Class 기반 Proxy 생성 시, 주의사항 (as  단점) =======
    super(null);
    // ======= 구체 Class 기반 Proxy 생성 시, 주의사항 (as  단점) =======
    this.target = target;
    this.logTrace = logTrace;
  }

  @Override
  public void orderItem(String itemId) {
    TraceStatus status = null;
    try{
      status = logTrace.begin("OrderService.orderItem()");
      // target 호출
      target.orderItem(itemId);
      logTrace.end(status);

    } catch(Exception e) {
      logTrace.exception(status, e);
      throw e; // 원래 Exception 은 그.대.로. 적용되게 만든다.
    }
  }
}
