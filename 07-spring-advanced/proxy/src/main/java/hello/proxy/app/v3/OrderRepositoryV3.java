package hello.proxy.app.v3;

import hello.proxy.app.domain.ItemConst;
import hello.proxy.util.CustomUtils;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryV3 {
  public void save(String itemId) {
    if (itemId.equals(ItemConst.exceptionId)) {
      throw new IllegalArgumentException("An exception occurred while saving !");
    }

    CustomUtils.sleep(1000);

  }
}
