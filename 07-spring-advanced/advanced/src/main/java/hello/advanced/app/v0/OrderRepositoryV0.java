package hello.advanced.app.v0;

import hello.advanced.app.v0.domain.itemIdConst;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryV0 {

  public void save(String itemId) {
    if (itemId.equals(itemIdConst.exception)) {
      throw new IllegalArgumentException("예외 발생!");
    }

    sleep(1000);// 상품 저장에 1초 걸린다고 가정
  }

  private void sleep(int millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
