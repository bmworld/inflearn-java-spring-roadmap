package hello.itemservice.web.basic;

import hello.itemservice.domain.Item;
import hello.itemservice.dto.UpdateItemRequestDto;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.IntStream;




/**
 * <h1>참고: HTML FORM: PUT, PATCH를 지원하지 않는다.</h1>
 */
@Controller
@RequestMapping(value = "/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

  private final ItemRepository itemRepository;

  @GetMapping
  public String getItems(Model model) {
    List<Item> items = itemRepository.findAll();
    model.addAttribute("items", items);
    return "basic/items";
  }


  @GetMapping("/{itemId}")
  public String getItem(@PathVariable long itemId, Model model) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/item";
  }


  // 상품 등록 => HTML FORM에서 action 에 값이 없으면, 현재 URL에 DATA 전송
  @GetMapping("/add")
  public String getAddItemForm(Model model) {
    return "basic/addForm";
  }


  // 상품등록 처리
//  @PostMapping("/add")
//  public String saveItemV1(@RequestParam String itemName,
//                         @RequestParam int price,
//                         @RequestParam int quantity,
//                         Model model) {
//    Item item = new Item(itemName, price, quantity);
//    itemRepository.save(item);
//    model.addAttribute("item", item);
//
//    return "basic/addForm";
//  }


  /**
   * @ModelAttribute postform 값을 Item 객체 생성,<br/>
   *                 요청 Param 값을 프로퍼티 접근법(setXxx)으로 넣어줌 <br/>
   *                  + Model에 객체 이름과 동일한 값을 알아서 넣어줌
   */
//  @PostMapping("/add")
//  public String saveItemV2(
//    @ModelAttribute("item") Item item,
////    Model model
//  ) {
//    itemRepository.save(item);
////    model.addAttribute("item", item); // <--- @ModelAttribute에 의해서 자동 추가 ( 생략 가능)
//    return "basic/addForm";
//  }


//  @PostMapping("/add")
//  public String saveItemV3(
//    @ModelAttribute Item item
//  ) {
//
//    itemRepository.save(item);
//    return "basic/addForm";
//  }



//  @PostMapping("/add")
//  public String saveItemV4(
//    Item item
//  ) {
//    // @ModelAttribute -> 객체이름 '첫 글자' -> 소문자로 변경해서, 객체 존재 시, 자동으로 값 넣어줌.
//    itemRepository.save(item);
//    return "basic/addForm";
//  }


  /**
   * @주의 상품 저장 후, 새로고침 시, 동일한 POST FormData가 계속 전송되는 이슈해결을 위해, REDIRECT 사용함!
   */
//  @PostMapping("/add")
//  public String saveItemV5(
//    Item item
//  ) {
//    itemRepository.save(item);
//    return "redirect:/basic/items/"+ item.getId();
//  }


  /**
   *
   * @param redirectAttributes 1) URL Encoding 2) `PathVariable` 3) `QueryParameter` 처리
   *  <br/>
   * @Example http://localhost:8080/basic/items/8?status=true
   */
  @PostMapping("/add")
  public String saveItemV6(
    Item item,
    RedirectAttributes redirectAttributes

  ) {

    Item savedItem = itemRepository.save(item);
    redirectAttributes.addAttribute("itemId", savedItem.getId());
    redirectAttributes.addAttribute("status", true);

    return "redirect:/basic/items/{itemId}";
  }






  // =================================================================================================
  // =================================================================================================

  @GetMapping("/{itemId}/edit")
  public String getEditForm(
    @PathVariable Long itemId,
    Model model
  ) {
    Item item = itemRepository.findById(itemId);
    model.addAttribute("item", item);
    return "basic/editForm";
  }


  @PostMapping("/{itemId}/edit")
  public String editForm(
    @PathVariable Long itemId,
    UpdateItemRequestDto requestDto,
    Model model) {
    Item item = itemRepository.findById(itemId);
    item.updateItem(requestDto);

    System.out.println("requestDto = " + requestDto);
    model.addAttribute("item", item);
    return "redirect:/basic/items/{itemId}";
  }






  @PostConstruct
  public void init() {
    int allItemCount = 5;
    IntStream.rangeClosed(1, allItemCount).forEach(i -> {
      String itemName = "item-" + i;
      int price = 10000 * i;
      int quantity = 10 * i;
      saveItem(itemName, price, quantity);
    });
  }


  private void saveItem(String name, int price, int quantity) {
    Item item = new Item(name, price, quantity);
    itemRepository.save(item);
  }
}
