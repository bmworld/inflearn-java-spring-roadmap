package hello.advanced.app.v3;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logTrace.LogTrace;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV3 {

  private final OrderServiceV3 orderService;
  private final LogTrace trace;

  @GetMapping("/v3/request")
  public ResponseEntity<?> request(String itemId) {

    System.out.println("================================================START================================================");
    TraceStatus status = null;
    try {
      status = trace.begin("OrderController.request()");
      orderService.orderItem(itemId);
      trace.end(status);
      return ResponseEntity.ok("v3 Request is DONE!");
    } catch (Exception e) {
      trace.exception(status, e);
      throw e; // 예외는 꼭, 다시, 던져줘야 한다.
      // try catch 로 Unchecked 예외를 먹고 정상흐름으로 하면, Application 흐름을 기존과 다르게 변경시키게 된 것이기 때문.
      // ( '해당 예제의 요구사항'과 맞지 않게 됨)
    }

  }

}
