package hello.advanced.app.v2;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.helloTrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {

  private final OrderServiceV2 orderService;
  private final HelloTraceV2 trace;

  @GetMapping("/v2/request")
  public ResponseEntity<?> request(String itemId) {

    System.out.println("================================================START================================================");
    TraceStatus status = null;
    try {
      status = trace.begin("OrderController.request()");
      orderService.orderItem(status.getTraceId(),itemId);
      trace.end(status);
      return ResponseEntity.ok("v1 Request is DONE!");
    } catch (Exception e) {
      trace.exception(status, e);
      throw e; // 예외는 꼭, 다시, 던져줘야 한다.
      // try catch 로 Unchecked 예외를 먹고 정상흐름으로 하면, Application 흐름을 기존과 다르게 변경시키게 된 것이기 때문.
      // ( '해당 예제의 요구사항'과 맞지 않게 됨)
    }

  }

}
