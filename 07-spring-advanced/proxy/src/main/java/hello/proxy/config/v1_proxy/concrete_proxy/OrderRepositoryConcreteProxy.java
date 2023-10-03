package hello.proxy.config.v1_proxy.concrete_proxy;

import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;


public class OrderRepositoryConcreteProxy extends OrderRepositoryV2 {

  private final OrderRepositoryV2 target;

  public OrderRepositoryConcreteProxy(OrderRepositoryV2 repository, LogTrace logTrace) {
    this.target = repository;
    this.logTrace = logTrace;
  }

  private final LogTrace logTrace;

  @Override
  public void save(String itemId) {
    TraceStatus status = null;
    try{
      status = logTrace.begin("OrderRepository.request()");
      // target 호출
      target.save(itemId);
      logTrace.end(status);

    } catch(Exception e) {
      logTrace.exception(status, e);
      throw e; // 원래 Exception 은 그.대.로. 적용되게 만든다.
    }

  }
}
