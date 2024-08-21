package hello.order.v2;

import hello.order.OrderService;
import io.micrometer.core.annotation.Counted;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceV2 implements OrderService {

  private final String METRIC_NAME = "my.order";
  private final AtomicInteger stock = new AtomicInteger(100);

  @Counted(METRIC_NAME)
  @Override
  public void order() {
    log.info("--- [v2] 주문");
    stock.decrementAndGet();
    // Counter > `micrometer` 에서 제공하는 것으로 써야함
  }

  @Counted(METRIC_NAME)
  @Override
  public void cancel() {
    log.info("--- [v2] cancel");
    stock.incrementAndGet();
  }

  @Override
  public AtomicInteger getStock() {
    return stock;
  }
}
