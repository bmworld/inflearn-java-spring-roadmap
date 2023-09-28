package hello.springtx.service;

import hello.springtx.exception.NotEnoughMoneyException;
import hello.springtx.order.Order;
import hello.springtx.order.OrderStatus;
import hello.springtx.order.testUserName;
import hello.springtx.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <h1>Business 요구사항</h1>
 * <pre>
 *   1. 정상: 주문 시, 결제성공하면 주문데이터를 저장하고 결제상태를 '완료'처리 함
 *   2. System Exception: 주문 시, 내부에 복구불가한 예외가 발생할 경우, 전체 데이터를 Rollback
 *   3. Business Exception: 주문 시 결제잔고 부족으로 결제 실패 시, 주문 데이터를 저장하고, 결제상태를 '대기'로 처리
 *     + 고객에게 잔고부족 알림 & 별도의 계좌 입금 안내
 *
 *     ( * 이 경우 Rollback하면, 주문 데이터가 날아간다. 따라서, Commit 처리를 반드시 해야한다)
 * </pre>
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class OrderService {
  private final OrderRepository orderRepository;



  @Transactional(rollbackFor = NotEnoughMoneyException.class )
  public void order(Order order) throws NotEnoughMoneyException {
    log.info("Order 호출");
    orderRepository.save(order);

    log.info("결제 Process 진입");
    if (order.getUsername().equals(testUserName.systemExUserName)) {
      log.info("시스템 예외 발생 => Rollback 됨");
      throw new RuntimeException("System Exception Occurred");
    } else if (order.getUsername().equals(testUserName.businessExUserName)) {
      log.info("잔고 부족 비즈니스 예외 발생 => Commit 됨 (public void order(Order order) throws NotEnoughMoneyException <-- 예외처리 해주었기 때문.)");
      order.setOrderStatus(OrderStatus.BEFORE_PAYMENT);
      throw new NotEnoughMoneyException("잔고가 부족합니다."); // !  Checked 예외이므로 Throws 던짐..
    } else{
      log.info("결제 승인");
      order.setOrderStatus(OrderStatus.PAYMENT_DONE);
    }


    log.info("--- Complete payment Process");


  }
}
