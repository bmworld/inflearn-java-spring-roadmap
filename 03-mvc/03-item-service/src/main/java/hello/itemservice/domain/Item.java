package hello.itemservice.domain;


import hello.itemservice.dto.UpdateItemRequestDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Item {
  private Long id;
  private String itemName;
  private Integer price;
  private Integer quantity;

  protected Item() {}

  public Item(String itemName, Integer price, Integer quantity) {
    this.itemName = itemName;
    this.price = price;
    this.quantity = quantity;
  }

  public void updateItem(UpdateItemRequestDto dto) {
    this.itemName = dto.getItemName();
    this.price = dto.getPrice();
    this.quantity = dto.getQuantity();
  }
}
