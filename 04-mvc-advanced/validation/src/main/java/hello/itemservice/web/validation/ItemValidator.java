package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ItemValidator implements Validator {
  @Override
  public boolean supports(Class<?> clazz) {
    // `isAssignableFrom`:  자식 클래스까지 모두 커버가 된다.
    // (Item.class == class 비교보다 낫다.)
    return Item.class.isAssignableFrom(clazz);
    // 만약 DataBinder 사용 & 복수의 Validator를 등록 시,
    // 해당 supports 메서드를 통해 검증대상 Class를 구별한다.
    // (*검증대상 = Controller 메서드에 붙은 @Validated의 타겟)
  }

  @Override
  public void validate(Object target, Errors errors) {

    Item item = (Item) target;
    if (!StringUtils.hasText(item.getItemName())) {
      errors.rejectValue("itemName", "required");
    }
    int minPrice = 1000;
    int maxPrice = 1000000;
    Integer price = item.getPrice();
    if (price == null || price < minPrice || price > maxPrice) {
      errors.rejectValue("price", "range", new Object[]{minPrice, maxPrice}, null);
    }

    Integer quantity = item.getQuantity();
    int maxQuantity = 9999;
    if (quantity == null || quantity >= maxQuantity) {
      errors.rejectValue("quantity", "max", new Object[]{maxQuantity}, null);
    }

    // 특정 필드가 아닌 북합오류 검증
    int minSalePrice = 10000;
    if (price != null && quantity != null) {
      int resultPrice = price * quantity;
      if (resultPrice < minSalePrice) {
        errors.reject("totalPriceMin", new Object[]{minSalePrice, resultPrice}, null);

      }
    }


  }
}
