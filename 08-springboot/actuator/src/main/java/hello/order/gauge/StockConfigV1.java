package hello.order.gauge;

import hello.order.OrderService;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class StockConfigV1 {

  @Bean
  public MyStockMetric myStockMetric(OrderService orderService, MeterRegistry meterRegistry) {
    return new MyStockMetric(orderService, meterRegistry);
  }

  @RequiredArgsConstructor
  public static class MyStockMetric {
    private final OrderService orderService;
    private final MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
      // micrometer 패키지 사용할 것
      Gauge.builder(
              "my.stock",
              orderService,
              (service) -> {
                log.info("[] stock gauge call");

                return service.getStock().get();
              })
          .register(meterRegistry);
    }
  }
}
