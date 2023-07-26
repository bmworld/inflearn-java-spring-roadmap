package hello.itemservice.validation;

import hello.itemservice.domain.item.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.IfProfileValue;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class BeanValidationTest {
  @DisplayName("Bean validation TEST - 검증기 생성 // 오류가 있을 경우에만 에러가 발생함. ")
  @Test
  void beanValidation () {
    ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    Validator validator = validatorFactory.getValidator();

    String invalidName = "  ";
    int invalidPrice = 0;
    int invalidQuantiry = 10000;
    Item item = new Item(invalidName, invalidPrice, invalidQuantiry);

    Set<ConstraintViolation<Item>> violations = validator.validate(item);
    for (ConstraintViolation<Item> violation : violations) {
      System.out.println("violation = " + violation);
      System.out.println("violation = " + violation.getMessage());
    }
  }
}
