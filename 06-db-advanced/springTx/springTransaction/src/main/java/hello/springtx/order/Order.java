package hello.springtx.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
  @Id
  @GeneratedValue
  private Long id;

  private String username;

  @Enumerated(EnumType.STRING)
  private OrderStatus orderStatus;
}
