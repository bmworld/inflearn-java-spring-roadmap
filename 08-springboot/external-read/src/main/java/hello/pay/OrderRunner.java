package hello.pay;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * `ApplicationRunner` Interface 사용 시, Spring 은 Bean 초기화 완료 후 , Application Loading 완료 시점에
 * `run(args)` 메서드를 호출한다.
 */
@Component
@RequiredArgsConstructor
public class OrderRunner implements ApplicationRunner {

  private final OrderService orderService;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    orderService.order(1000);
  }
}
