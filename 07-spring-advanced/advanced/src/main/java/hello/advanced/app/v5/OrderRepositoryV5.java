package hello.advanced.app.v5;

import hello.advanced.app.domain.itemIdConst;
import hello.advanced.trace.callback.TraceTemplate;
import hello.advanced.trace.logTrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import org.springframework.stereotype.Repository;

import static hello.util.customUtils.sleep;

@Repository
public class OrderRepositoryV5 {

  private final TraceTemplate template;

  public OrderRepositoryV5(LogTrace logTrace) {
    this.template = new TraceTemplate(logTrace);
  }

  public void save(String itemId) {

    template.execute("OrderRepository.save()", () -> {
      if (itemId.equals(itemIdConst.exception)) {
        throw new IllegalArgumentException("예외 발생!");
      }
      sleep(1000);// 상품 저장에 1초 걸린다고 가정
      return null;
    });
  }

}
