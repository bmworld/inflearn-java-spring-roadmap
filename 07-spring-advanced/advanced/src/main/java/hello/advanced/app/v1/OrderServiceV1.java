package hello.advanced.app.v1;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.helloTrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV1 {
  private final OrderRepositoryV1 orderRepository;
  private final HelloTraceV1 trace;

  public void orderItem(String itemId) {
    TraceStatus status = null;
    try {
      status = trace.begin("OrderService.orderItem()");
      orderRepository.save(itemId);
      trace.end(status);
    } catch (Exception e) {
      trace.exception(status, e);
      throw e; // 예외는 꼭, 다시, 던져줘야 한다.
      // try catch 로 Unchecked 예외를 먹고 정상흐름으로 하면, Application 흐름을 기존과 다르게 변경시키게 된 것이기 때문.
      // ( '해당 예제의 요구사항'과 맞지 않게 됨)
    }



  }
}
