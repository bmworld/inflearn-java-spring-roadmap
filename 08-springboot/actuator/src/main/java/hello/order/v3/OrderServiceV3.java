package hello.order.v3;

import hello.order.OrderService;
import hello.utils.ThreadUtils;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceV3 implements OrderService {

  private final MeterRegistry meterRegistry;

  private final String METRIC_NAME = "my.order";
  private AtomicInteger stock = new AtomicInteger(100);

  @Override
  public void order() {
    Timer timer =
        Timer.builder(METRIC_NAME)
            .tag("class", this.getClass().getName())
            .tag("method", "order")
            .description("order")
            .register(this.meterRegistry);

    timer.record(
        () -> {
          log.info("--- 주문");
          stock.decrementAndGet();
          ThreadUtils.sleep(500, 1000);
        });
  }

  @Override
  public void cancel() {

    Timer timer =
        Timer.builder(METRIC_NAME)
            .tag("class", this.getClass().getName())
            .tag("method", "cancel")
            .description("order")
            .register(this.meterRegistry);

    timer.record(
        () -> {
          log.info("--- 취소");
          stock.incrementAndGet();
          ThreadUtils.sleep(200, 200);
        });
  }

  @Override
  public AtomicInteger getStock() {
    return stock;
  }
}
