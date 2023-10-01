package hello.advanced.app.v4;

import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logTrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV4 {

  private final OrderServiceV4 orderService;
  private final LogTrace trace;

  @GetMapping("/v4/request")
  public ResponseEntity<?> request(String itemId) {
    AbstractTemplate<ResponseEntity<?>> template = new AbstractTemplate<>(trace) {
      @Override
      protected ResponseEntity call() {
        orderService.orderItem(itemId);
        return ResponseEntity.ok("v4 Request is DONE!");
      }
    };

    return template.execute("OrderController.request()");

  }

}
