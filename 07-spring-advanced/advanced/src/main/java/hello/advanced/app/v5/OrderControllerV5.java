package hello.advanced.app.v5;

import hello.advanced.trace.callback.TraceCallback;
import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logTrace.LogTrace;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerV5 {

  private final OrderServiceV5 orderService;
  private final TraceTemplate template;

  public OrderControllerV5(OrderServiceV5 orderService, LogTrace logTrace) {
    this.orderService = orderService;
    this.template = new TraceTemplate(logTrace);
  }

  @GetMapping("/v5/request")
  public ResponseEntity<?> request(String itemId) {
    return (ResponseEntity<?>) template.execute("OrderController.request()", new TraceCallback<>() {
      @Override
      public ResponseEntity<String> call() {
        orderService.orderItem(itemId);
        return ResponseEntity.ok("v5 Request is DONE!");
      }
    });
  }

}
