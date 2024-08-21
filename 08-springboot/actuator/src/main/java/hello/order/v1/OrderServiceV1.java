package hello.order.v1;

import hello.order.OrderService;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceV1 implements OrderService {

  private final MeterRegistry meterRegistry;
  private final String COUNTER_DESCRIPTION = "주문 설명";
  private final AtomicInteger stock = new AtomicInteger(100);

  @Override
  public void order() {
    log.info("--- [v1] 주문");
    stock.decrementAndGet();
    // Counter > `micrometer` 에서 제공하는 것으로 써야함

    Counter.builder("my.order")
        .tag("class", this.getClass().getName())
        .tag("method", "order")
        .description(COUNTER_DESCRIPTION) // 하나의 counter.name 에 대해 1가지만 가능
        .register(this.meterRegistry)
        .increment(); // increment() -> metric 카운터 값 하나 증가
  }

  @Override
  public void cancel() {
    log.info("--- [v1] cancel");
    stock.incrementAndGet();

    Counter.builder("my.order")
        .tag("class", this.getClass().getName())
        .tag("method", "cancel")
        .description(COUNTER_DESCRIPTION)
        .register(this.meterRegistry)
        .increment(); // increment() -> metric 카운터 값 하나 증가
  }

  @Override
  public AtomicInteger getStock() {
    return stock;
  }
}
