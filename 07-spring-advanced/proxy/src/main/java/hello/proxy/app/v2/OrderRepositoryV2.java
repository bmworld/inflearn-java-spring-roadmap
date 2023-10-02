package hello.proxy.app.v2;

import hello.proxy.app.domain.ItemConst;
import hello.proxy.util.CustomUtils;

public class OrderRepositoryV2{
  public void save(String itemId) {
    if (itemId.equals(ItemConst.exceptionId)) {
      throw new IllegalArgumentException("An exception occurred while saving !");
    }

    CustomUtils.sleep(1000);

  }
}
