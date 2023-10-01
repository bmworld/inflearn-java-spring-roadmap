package hello.advanced.app.v4;

import hello.advanced.app.domain.itemIdConst;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.logTrace.LogTrace;
import hello.advanced.trace.template.AbstractTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static hello.util.customUtils.sleep;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV4 {

  private final LogTrace trace;

  public void save(String itemId) {

    AbstractTemplate<Void> template = new AbstractTemplate<>(trace) {
      @Override
      protected Void call() {
        if (itemId.equals(itemIdConst.exception)) {
          throw new IllegalArgumentException("예외 발생!");
        }
        sleep(1000);// 상품 저장에 1초 걸린다고 가정
        return null;
      }
    };

    template.execute("OrderRepository.save()");
  }

}
