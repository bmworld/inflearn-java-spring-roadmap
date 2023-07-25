package hello.itemservice.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * <h1>MessageCodesResolver</h1>
 * <pre>
 *   검증 오류 메시지코드들을 생성한다.
 *   `MessageCodesResolver` 인터페이스고 `DefaultMessageCodesResolver`는 기본 구현체다.
 *
 * </pre>
 * <h2>DefaultMessageCodesResolver의 기본 메시지 생성 규칙</h2>
 * <pre>
 *   ---- 객체 오류 ----
 *   1. code + "." + object name
 *   2. code
 *
 *   ex) 오류코드: required, object name: item
 *   1. required.item
 *   2. required
 *
 *
 *
 *
 *   ---- 필드 오류 ----
 *  1. code + "." + object name + "." + field
 *  2. code + "." + field
 *  3. code + "." + field type
 *  4. code
 *
 *  ex) 오류코드: typeMismatch, object name "user", field "age", field type: int
 *  1. "typeMismatch.user.age"
 *  2. "typeMismatch.age"
 *  3. "typeMismatch.int"
 *  4. "typeMismatch"
 *
 *
 * ----------------------------- 실제 Example --------------------------------
 * Field error in object 'item' on field 'itemName': rejected value [];
 *   codes [
 *   required.item.itemName,
 *   required.itemName,
 *   required.java.lang.String,
 *   required
 *   ];
 *   arguments []; default message [null]
 *
 * </pre>
 */
public class MessageCodeResolverTest {
  MessageCodesResolver codesResolver = new DefaultMessageCodesResolver();


  @DisplayName("MessageCodeResolver TEST")
  @Test
  void messageCodesResolverTest() {
    // Given
    String[] messageCodes = codesResolver.resolveMessageCodes("required", "item");
    System.out.println("messageCodes = " + messageCodes);

    for (String messageCode : messageCodes) {
      System.out.println("messageCode = " + messageCode);
      /** RESULT
       * messageCode = required.item
       * messageCode = required
       * => new ObjectError("item", new String[]{"required.item", "required"});// 객체 Depth가 깊은 Detail한 것을 우선으로 넣는다. (BindingResult 에서 그렇게 처리하고 있다.)
       */
    }

    assertThat(messageCodes).containsExactly("required.item", "required");
  }

  @DisplayName("messageCodesResolverField")
  @Test
  void messageCodesResolverField() {

    String[] messageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String.class);
    for (String messageCode : messageCodes) {
      System.out.println("messageCode = " + messageCode);
      /** RESULT => 이것과 동일 => BindingResult.rejectValue("itemName", "required");
       * messageCode = required.item.itemName => 개발자가 직접 작성한 detail Error Message
       * messageCode = required.itemName => 개발자가 직접 작성한 덜 자세한 Error Message
       * messageCode = required.java.lang.String => BindingResult가 알아서 만들어준 것
       * messageCode = required => 완전 범용적임
       */
    }

    assertThat(messageCodes).containsExactly(
      "required.item.itemName",
              "required.itemName",
              "required.java.lang.String",
              "required"
    );

  }
}
