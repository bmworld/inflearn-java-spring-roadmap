package hello.advanced.app.v1;

import hello.advanced.app.domain.itemIdConst;
import hello.advanced.trace.TraceStatus;
import hello.advanced.trace.helloTrace.HelloTraceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV1 {

  private final HelloTraceV1 trace;

  public void save(String itemId) {

    TraceStatus status = null;
    try {
      status = trace.begin("OrderRepository.save()");


      // ====
      if (itemId.equals(itemIdConst.exception)) {
        throw new IllegalArgumentException("예외 발생!");
      }
      sleep(1000);// 상품 저장에 1초 걸린다고 가정

      trace.end(status);
    } catch (Exception e) {
      trace.exception(status, e);
      throw e; // 예외는 꼭, 다시, 던져줘야 한다.
      // try catch 로 Unchecked 예외를 먹고 정상흐름으로 하면, Application 흐름을 기존과 다르게 변경시키게 된 것이기 때문.
      // ( '해당 예제의 요구사항'과 맞지 않게 됨)
    }
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
