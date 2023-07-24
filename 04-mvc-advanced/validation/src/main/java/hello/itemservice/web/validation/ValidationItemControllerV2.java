package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

  private final ItemRepository itemRepository;

  @GetMapping
  public String items(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "validation/v2/items";
  }

  @GetMapping("/{itemId}")
  public String item(@PathVariable long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v2/item";
  }

  @GetMapping("/add")
  public String addForm(Model model) {
    model.addAttribute("item", new Item());
    return "validation/v2/addForm";
  }


  /**
   * <h1>BindingResult : spring이 view에 전달해줌</h1>
   * <h1>BindingResult Parameter 위치는 반드시 @ModelAttribute 바로 다음에 위치해야한다.</h1>
   */
//  @PostMapping("/add")
//  public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//
//    // 검증 오류 결과를 보관한다.
//    // 검증 로직
//    if (!StringUtils.hasText(item.getItemName())) {
//      bindingResult.addError(new FieldError("item", "itemName", "상품이름은 필수입니다."));
//    }
//
//    Integer price = item.getPrice();
//    if (price == null || price < 1000 || price > 1000000) {
//      bindingResult.addError(new FieldError("item", "price", "가격은 1,000 ~ 1,000,000원 사이까지 허용됩니다."));
//    }
//
//    Integer quantity = item.getQuantity();
//    if (quantity == null || quantity >= 9999) {
//      bindingResult.addError(new FieldError("item", "quantity", "최대 허용 수량은 9,999개 입니다."));
//    }
//
//    // 특정 필드가 아닌 북합오류 검증
//    int minSalePrice = 10000;
//    if (price != null && quantity != null) {
//      int resultPrice = price * quantity;
//      if (resultPrice < minSalePrice) {
//        bindingResult.addError(new ObjectError("item", "가격  * 수량의 합은" + minSalePrice + "이상이어야 합니다. 현재 값 = " + resultPrice));
//      }
//    }
//
//    // 검증 실패 시, 다시 입력 폼으로
//    if (bindingResult.hasErrors()) {
//      log.info("errors={}", bindingResult);
//      return "validation/v2/addForm";
//    }
//
//
//    // 성공 로직
//    Item savedItem = itemRepository.save(item);
//    redirectAttributes.addAttribute("itemId", savedItem.getId());
//    redirectAttributes.addAttribute("status", true);
//    return "redirect:/validation/v2/items/{itemId}";
//  }


  /**
   * <h1>FieldError 생성자</h1>
   * @objectName 오류가 발생한 객체 이름
   * @field 오류가 발생한 필드
   * @rejectedValue 거절된 값 = 사용자가 입력한 값 (오류 전의 DATA를 재사용하기 위함)
   * @bindingFailure 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분한 값
   * @codes 메시지 코드
   * @arguments 메시지에서 사용하는 인자
   * @defaultMessage 기본 오류 메시지
   *
   *
   * <h1>Thymeleaf</h1>
   * <pre>
   * Thymeleaf의 사용자 값 유지
   * 타임 리프의 th:filed눈 매우 영리하게 동작한다.
   * 정상 상황에서는 모델 객체의 값을 사용
   * 오류 발생 시, `fieldError`에서 보관한 값을 출력한다.
   * </pre>
   *
   */

  @PostMapping("/add")
  public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

    // 검증 오류 결과를 보관한다.
    // 검증 로직
    if (!StringUtils.hasText(item.getItemName())) {
      bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),false,null, null, "상품이름은 필수입니다."));
    }

    Integer price = item.getPrice();
    if (price == null || price < 1000 || price > 1000000) {
      bindingResult.addError(new FieldError("item", "price", item.getPrice(),false,null, null,"가격은 1,000 ~ 1,000,000원 사이까지 허용됩니다."));
    }

    Integer quantity = item.getQuantity();
    if (quantity == null || quantity >= 9999) {
      bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),false,null, null,"최대 허용 수량은 9,999개 입니다."));
    }

    // 특정 필드가 아닌 북합오류 검증
    int minSalePrice = 10000;
    if (price != null && quantity != null) {
      int resultPrice = price * quantity;
      if (resultPrice < minSalePrice) {
        bindingResult.addError(new ObjectError("item", null, null, "가격  * 수량의 합은" + minSalePrice + "이상이어야 합니다. 현재 값 = " + resultPrice));
      }
    }

    // 검증 실패 시, 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      log.info("errors={}", bindingResult);
      return "validation/v2/addForm";
    }


    // 성공 로직
    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);
    return "redirect:/validation/v2/items/{itemId}";
  }

  @GetMapping("/{itemId}/edit")
  public String editForm(@PathVariable Long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "validation/v2/editForm";
  }

  @PostMapping("/{itemId}/edit")
  public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
    itemRepository.update(itemId, item);
    return "redirect:/validation/v2/items/{itemId}";
  }

}
