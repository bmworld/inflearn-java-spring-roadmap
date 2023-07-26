package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 * @ScriptAssert 기타 검증을 추가할 수 있으나,,,,,, 실무에서 쓰기에는 적합하지 않다.
 */
//
//@ScriptAssert(lang="javascript", script ="_this.price * _this.quantity >= 10000", message = "결제가격 10,000원 이상으로 입력해주세요.")
@Data
public class Item {

    @NotNull(groups = {UpdateCheck.class}) // 수정, 생성 등에서 각각 Validation 을 적용하려는 경우
    private Long id;

//    @NotBlank(message = "Custom Error Message!")
    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Range(min = 1000, max = 1000000)
    private Integer price;

    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
    @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
