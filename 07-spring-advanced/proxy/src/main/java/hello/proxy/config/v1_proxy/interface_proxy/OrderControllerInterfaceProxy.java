package hello.proxy.config.v1_proxy.interface_proxy;

import hello.proxy.app.v1.OrderControllerV1;
import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class OrderControllerInterfaceProxy implements OrderControllerV1 {
  private final OrderControllerV1 target;

  private final LogTrace logTrace;
  @Override
  public String request(String itemId) {
    TraceStatus status = null;
    try{
      status = logTrace.begin("OrderRepository.request()");
      // target 호출
      String result = target.request(itemId);
      logTrace.end(status);

      return result;
    } catch(Exception e) {
      logTrace.exception(status, e);
      throw e; // 원래 Exception 은 그.대.로. 적용되게 만든다.
    }
  }

  @Override
  public String noLog() {
    // 예제 의도: 해당 API는 보안상 이슈로, 로그를 찍지 않는다.
    return target.noLog();
  }

}
