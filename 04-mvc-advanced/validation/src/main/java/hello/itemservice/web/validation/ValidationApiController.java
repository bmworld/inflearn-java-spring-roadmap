package hello.itemservice.web.validation;

import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController // =  @Controller + @ResponseBody
@Slf4j
@RequestMapping("/validation/api/items")
public class ValidationApiController {

  /**
   *
   * <pre>
   * Request Field Type이 안맞아서, Request Object 생성 실패할 경우, Controller 자체가 호출되지 않는다!!
   * 따라서, Validation 이 작동할 수도 없다.
   * </pre>
   */
  @PostMapping("/add")
  public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {

    log.info("API CONTROLLER > addItem");

    if (bindingResult.hasErrors()) {
      return bindingResult.getAllErrors(); // field Error, ObjectError 모두 반환해줌
    }


    log.info("SUCCESS API RESPONSE");
    return form;
  }


  /**
   * <h1>@ModelAttribute</h1>
   * <pre>
   *   - 각각의 필드 단위로 세밀하게 적용된다
   *   - 따라서 특정 필드에 타입이 맞지 않는 오류가 발생하더라도,
   *     나머지 필드는 정상적으로 처리 가능
   * </pre>
   * <h1>@RequestBody</h1>
   * <pre>
   *   - `HttpMessageConverter` 단계에서 JSON 객체로 일단 변경시켜야한다.
   *   - 변경하지 못하면, 이후 단계를 진행하지 못한다.
   *     => 그 결과 Controller 및 Validator 적용 불가하다.
   * </pre>
   */
  @PostMapping("/update")
  public Object updateItem(@RequestBody @Validated ItemUpdateForm form, BindingResult bindingResult) {

    log.info("API CONTROLLER > updateItem");

    if (bindingResult.hasErrors()) {
      return bindingResult.getAllErrors(); // field Error, ObjectError 모두 반환해줌
    }


    log.info("SUCCESS API RESPONSE");
    return form;
  }
}
