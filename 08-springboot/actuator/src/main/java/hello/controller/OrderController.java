package hello.controller;

import hello.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {

  public final OrderService orderService;

  @PostMapping("/order")
  public String order() {
    log.info("--- OrderController > order");
    orderService.order();
    return "--- order completed";
  }

  @PostMapping("/cancel")
  public String cancel() {
    log.info("--- OrderController > cancel");
    orderService.cancel();
    return "--- order canceled";
  }

  @GetMapping("/stock")
  public Integer stock() {
    log.info("--- OrderController > stock");
    return orderService.getStock().get();
  }
}
