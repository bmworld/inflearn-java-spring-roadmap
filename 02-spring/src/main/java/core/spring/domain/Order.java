package core.spring.domain;

public class Order {
  private Long memberId;
  private String itemName;
  private int itemPrice;
  private int discountPrice;

  public Order(Long id, String itemName, int itemPrice, int discountPrice) {
    this.memberId = id;
    this.itemName = itemName;
    this.itemPrice = itemPrice;
    this.discountPrice = discountPrice;
  }

  // 비즈니스 계산로직

  public int calculatePrice() {
    return itemPrice - discountPrice;
  }
  public Long getMemberId() {
    return memberId;
  }

  public void setMemberId(Long memberId) {
    this.memberId = memberId;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public int getItemPrice() {
    return itemPrice;
  }

  public void setItemPrice(int itemPrice) {
    this.itemPrice = itemPrice;
  }

  public int getDiscountPrice() {
    return discountPrice;
  }

  public void setDiscountPrice(int discountPrice) {
    this.discountPrice = discountPrice;
  }

  @Override
  public String toString() {
    return "Order{" +
      "id=" + memberId +
      ", itemName='" + itemName + '\'' +
      ", itemPrice=" + itemPrice +
      ", discountPrice=" + discountPrice +
      '}';
  }
}
