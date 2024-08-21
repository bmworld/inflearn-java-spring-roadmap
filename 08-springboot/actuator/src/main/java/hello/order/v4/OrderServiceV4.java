package hello.order.v4;

import hello.order.OrderService;
import hello.utils.ThreadUtils;
import io.micrometer.core.annotation.Timed;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 *
 * <h1>[AOP] micrometer - Timer 적용</h1>
 *
 * - 해당 Class 내부의 모든 Method 일괄적용됨
 */
@Timed("my.order")
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceV4 implements OrderService {

  private AtomicInteger stock = new AtomicInteger(100);

  @Override
  public void order() {
    log.info("--- 주문");
    stock.decrementAndGet();
    ThreadUtils.sleep(500, 1000);
  }

  @Override
  public void cancel() {

    log.info("--- 취소");
    stock.incrementAndGet();
    ThreadUtils.sleep(200, 200);
  }

  @Override
  public AtomicInteger getStock() {
    return stock;
  }
}
