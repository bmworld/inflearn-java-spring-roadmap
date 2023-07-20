package hello.itemservice.domain.item;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *  ** FORM 사용할 수 있는 방식이
 *  ENUM, 일반 class 등 여러 방식을 사용할 수 있다는 것을 보여주기 위한 예시로서 해당 클래스가 존재한다.
 *  ****************************************************************
 *
 *
 *
 * FAST: 빠른 배송
 * NORMAL: 일반 배송
 * SLOW: 느린 배송
 */
@Data
@AllArgsConstructor
public class DeliveryCode {
  private String code;
  private String displayName;
}
