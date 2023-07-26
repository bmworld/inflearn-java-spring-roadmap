package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("validation/v3/items")
@RequiredArgsConstructor
public class ValidationItemControllerV3 {

  private final ItemRepository itemRepository;


  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "validation/v3/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v3/item";
  }


  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v3/editForm";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("item", new Item());
    return "validation/v3/addForm";
  }


  /**
   * <h1>LocalValidatorFactoryBean</h1>
   * <pre>
   *   - `LocalValidatorFactoryBean`을 Global Validator 로서 등록한다.
   *   - 이 `Validator`는 @NotNull 같은 Annotation을 보고 검증을 수행
   *   - Global Validation가 등록되었으므로, @Valid, @Validated 만 적용하면 작동한다.
   *   - 검증 오류 발생 시, `FiledError`, `ObjectError`를 생성해서
   *     `BindingResult`에 담아준다.
   *   - `@Validated`
   *     'org.springframework.boot:spring-boot-starter-validation' 라이브러리 추가 시,
   *     BeanValidator를 자동으로 인식하고, 스프링에 통합한다.
   * </pre>
   *
   * <h1>검증 순서</h1>
   * <pre>
   *   1. `@ModelAttribute` 각각 필드에 타입 변환 시도
   *   1-1. 성공 시, 다음으로
   *   1-2. 실패 시, `typeMismatch`를 `FieldError`에 추가
   *   2. Validator 적용
   *
   *   * 이때, Binding에 성공한 Field만 Bean Validation 적용함
   *   - BeanValiator는 Binding에 실패한 필드는 BeanValidation을 적용하지 않는다.
   *     즉, Type 변환에 성공하여 Binding에 성공한 Field 여야만, BeanValidation 적용이 유의미하게 된다.
   *      ( 일단 모델 객체에 바인딩 받는 값이 정상으로 받아져야, 이후의 검증이 의미있다는 의미)
   *
   *   - FLOW: `@ModelAttribute` -> 각 필드 타입 변환 시도 -> 변환에 성공한 Field만 BeanValidation 적용
   *
   *   - Ex1) `itemName` Field 문자 입력 -> 타입변환 성공
   *           -> `itemName` 필드에 BeanValidation 적용
   *   - Ex2) `itemName` Field 숫자 입력 -> 타입변환 실패
   *           -> typeMismatch FieldError 추가 -> `itemName` BeanValidation 적용 X
   *
   * </pre>
   */
// ================================= addItemV1 =================================
//  @PostMapping("/add")
  public String addItemV1(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {


    // 특정 필드가 아닌 북합오류 검증
    int minSalePrice = 10000;
    if (item.getPrice() != null  && item.getQuantity() != null) {
      int price = item.getPrice();
      int quantity = item.getQuantity();
      int resultPrice = price * quantity;
      if (resultPrice < minSalePrice) {
        bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice},null);
      }
    }

    // 검증 실패 시, 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      log.info("errors={}", bindingResult);
      return "/validation/v3/addForm";
    }


    // 성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v3/items/{itemId}";
  }






  // ================================= Edit Item V1 =================================
//  @PostMapping("/{itemId}/edit")
  public String editV1(@PathVariable Long itemId, @Validated @ModelAttribute Item item, BindingResult bindingResult) {

    // 특정 필드가 아닌 북합오류 검증
    int minSalePrice = 10000;
    if (item.getPrice() != null  && item.getQuantity() != null) {
      int price = item.getPrice();
      int quantity = item.getQuantity();
      int resultPrice = price * quantity;
      if (resultPrice < minSalePrice) {
        bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice},null);
      }
    }

    System.out.println("bindingResult = " + bindingResult);

    // 검증 실패 시, 다시 입력 수정 폼으로
    if (bindingResult.hasErrors()) {
      System.out.println("검증 오류 존재");
      log.info("errors={}", bindingResult);
      return "validation/v3/editForm";
    }


    System.out.println(" 검증 통과 " + item );


    itemRepository.update(itemId, item);
    return "redirect:/validation/v3/items/{itemId}";
  }







  // ================================= Add Item V2 =================================
  @PostMapping("/add")
  public String addItemV2(@Validated(value = SaveCheck.class) @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {


    // 특정 필드가 아닌 북합오류 검증
    int minSalePrice = 10000;
    if (item.getPrice() != null  && item.getQuantity() != null) {
      int price = item.getPrice();
      int quantity = item.getQuantity();
      int resultPrice = price * quantity;
      if (resultPrice < minSalePrice) {
        bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice},null);
      }
    }

    // 검증 실패 시, 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      log.info("errors={}", bindingResult);
      return "/validation/v3/addForm";
    }


    // 성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v3/items/{itemId}";

  }


  // ================================= Edit Item V2 =================================

  @PostMapping("/{itemId}/edit")
  public String editV2(@PathVariable Long itemId, @Validated(value = UpdateCheck.class) @ModelAttribute Item item, BindingResult bindingResult) {

    // 특정 필드가 아닌 북합오류 검증
    int minSalePrice = 10000;
    if (item.getPrice() != null  && item.getQuantity() != null) {
      int price = item.getPrice();
      int quantity = item.getQuantity();
      int resultPrice = price * quantity;
      if (resultPrice < minSalePrice) {
        bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice},null);
      }
    }

    System.out.println("bindingResult = " + bindingResult);

    // 검증 실패 시, 다시 입력 수정 폼으로
    if (bindingResult.hasErrors()) {
      System.out.println("검증 오류 존재");
      log.info("errors={}", bindingResult);
      return "validation/v3/editForm";
    }


    System.out.println(" 검증 통과 " + item );


    itemRepository.update(itemId, item);
    return "redirect:/validation/v3/items/{itemId}";
  }
}
