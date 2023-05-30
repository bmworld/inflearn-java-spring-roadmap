package core.spring.service;

import core.spring.domain.Order;

public interface OrderService {
  Order createOrder(Long memberId, String itemName, int itemPrice);
}
