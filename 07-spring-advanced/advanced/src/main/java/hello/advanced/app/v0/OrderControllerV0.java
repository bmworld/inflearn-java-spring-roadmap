package hello.advanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV0 {
  private final OrderServiceV0 orderService;

  @GetMapping("/v0/request")
  public ResponseEntity<?> request(String itemId) {
    orderService.orderItem(itemId);
    return ResponseEntity.ok("OK!!");
  }

}
