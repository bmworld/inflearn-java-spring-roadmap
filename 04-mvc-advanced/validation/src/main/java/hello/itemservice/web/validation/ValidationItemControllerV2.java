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
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
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

  private final ItemValidator validator;


  /**
   * <h1>@InitBinder</h1>
   * <pre>
   *   - 해당 컨트롤러에만 영향을 준다.
   *   - 글로벌 설정은 별도로 해줘야함
   * </pre>
   */
  @InitBinder
  public void init(
    WebDataBinder dataBinder
  ) {
    // Spring의 도움을 받아서
    // 적용시킬 controller 내부 메서드의 검증 대상 객체에 `@Validated `를 붙인다.
    // 검증 결과는 BindingResult 에 담겨진다.
    dataBinder.addValidators(validator);
  }
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


  ////================================================================================================================

//  @PostMapping("/add")
//  public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//
//    // 검증 오류 결과를 보관한다.
//    // 검증 로직
//    if (!StringUtils.hasText(item.getItemName())) {
//      bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),false,null, null, "상품이름은 필수입니다."));
//    }
//
//    Integer price = item.getPrice();
//    if (price == null || price < 1000 || price > 1000000) {
//      bindingResult.addError(new FieldError("item", "price", item.getPrice(),false,null, null,"가격은 1,000 ~ 1,000,000원 사이까지 허용됩니다."));
//    }
//
//    Integer quantity = item.getQuantity();
//    if (quantity == null || quantity >= 9999) {
//      bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),false,null, null,"최대 허용 수량은 9,999개 입니다."));
//    }
//
//    // 특정 필드가 아닌 북합오류 검증
//    int minSalePrice = 10000;
//    if (price != null && quantity != null) {
//      int resultPrice = price * quantity;
//      if (resultPrice < minSalePrice) {
//        bindingResult.addError(new ObjectError("item", null, null, "가격  * 수량의 합은" + minSalePrice + "이상이어야 합니다. 현재 값 = " + resultPrice));
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


//  @PostMapping("/add")
//  public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//
//    log.info("objectName={}", bindingResult.getObjectName());
//    log.info("target={}", bindingResult.getTarget());
//    // 검증 오류 결과를 보관한다.
//    // 검증 로직
//    if (!StringUtils.hasText(item.getItemName())) {
//      bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),false, new String[]{"required.item.itemName"}, null, null));
//    }
//
//    int minPrice = 1000;
//    int maxPrice = 1000000;
//    Integer price = item.getPrice();
//    if (price == null || price < minPrice || price > maxPrice) {
//      bindingResult.addError(new FieldError("item", "price", item.getPrice(),false,new String[]{"range.item.price"}, new Object[]{minPrice, maxPrice},null));
//    }
//
//    Integer quantity = item.getQuantity();
//    int maxQuantity = 9999;
//    if (quantity == null || quantity >= maxQuantity) {
//      bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),false, new String[]{"max.item.quantity"}, new Object[]{maxQuantity},null));
//    }
//
//    // 특정 필드가 아닌 북합오류 검증
//    int minSalePrice = 10000;
//    if (price != null && quantity != null) {
//      int resultPrice = price * quantity;
//      if (resultPrice < minSalePrice) {
//        bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{minSalePrice, resultPrice}, null));
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
   * BindingResult => 이미 어떤 객체를 검증해야할 지 알고있다.
   * 따라서
   * BindingResult가 제공하는 RejectValue(), reject() 사용 시, `FieldError`, `ObjectError`를 직접 생성하지 않고, 깔끔하게 검증 오류를 다룰 수 있다.
   */
//  @PostMapping("/add")
//  public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//
//
//    System.out.println("bindingResult.getAllErrors() = " + bindingResult.getAllErrors());
//    // Type 에러 등의 오류가 이미 존재할 경우, 먼저 걸러 줄 수 있다.
//    if (bindingResult.hasErrors()) {
//      log.info("errors={}", bindingResult);
//      return "validation/v2/addForm";
//    }
//
//
//    // =================================================================================================
//    // =================================================================================================
//    // =================================================================================================
//    // =================================================================================================
//    // =================================================================================================
//    // -------- UTILITY 가 존재한다.// 단순한 기능정도만 제공함 --------
//    // SAME Validation
////    ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");
//    if (!StringUtils.hasText(item.getItemName())) {
//      bindingResult.rejectValue("itemName", "required");
//    }
//
//    // =================================================================================================
//    // =================================================================================================
//
//    int minPrice = 1000;
//    int maxPrice = 1000000;
//    Integer price = item.getPrice();
//    if (price == null || price < minPrice || price > maxPrice) {
//      bindingResult.rejectValue("price", "range", new Object[]{minPrice, maxPrice}, null);
//    }
//
//    Integer quantity = item.getQuantity();
//    int maxQuantity = 9999;
//    if (quantity == null || quantity >= maxQuantity) {
//      bindingResult.rejectValue("quantity", "max", new Object[]{maxQuantity}, null);
//    }
//
//    // 특정 필드가 아닌 북합오류 검증
//    int minSalePrice = 10000;
//    if (price != null && quantity != null) {
//      int resultPrice = price * quantity;
//      if (resultPrice < minSalePrice) {
//        bindingResult.reject("totalPriceMin", new Object[]{minSalePrice, resultPrice}, null);
//
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



//  @PostMapping("/add")
//  public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
//
//
//    validator.validate(item, bindingResult);
//
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
   * <h1>@Validated</h1>
   * <pre>
   *   - 검증기(validator)를 실행하라는 Annotation
   *   - @WebDataBinder 에 등록한 validator를 찾아서 실행한다>
   *   - @WebDataBinderㅇ 에 여러 Validator가 등록된 경우에는 Class 구분로직
   *    => @Override public boolean supports(){...} 에 등록된 validator 가 적용된다.
   * </pre>
   */
  @PostMapping("/add")
  public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {


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
}
