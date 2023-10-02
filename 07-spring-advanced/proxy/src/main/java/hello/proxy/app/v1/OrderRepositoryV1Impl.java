package hello.proxy.app.v1;

import hello.proxy.app.domain.ItemConst;
import hello.proxy.util.CustomUtils;

public class OrderRepositoryV1Impl implements OrderRepositoryV1{
  @Override
  public void save(String itemId) {
    if (itemId.equals(ItemConst.exceptionId)) {
      throw new IllegalArgumentException("An exception occurred while saving !");
    }

    CustomUtils.sleep(1000);

  }
}
