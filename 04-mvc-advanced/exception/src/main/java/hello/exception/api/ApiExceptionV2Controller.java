package hello.exception.api;

import hello.exception.exception.CustomBadRequestException;
import hello.exception.exception.CustomException;
import hello.exception.exceptionHandler.ErrorResult;
import jdk.jshell.spi.ExecutionControl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


/**
 * <h1>Exception 우선순위</h1>
 * <pre>
 *   스프링의 우선순위는 항상 자세한 것이 우선권을 가진다
 *
 *   ex. 부모예외처리() & 자식 예외처리() 모두 호출된 경우
 *   그러나 더 자세한 것( 여기서는 자식)이 우선권을 가지므로,
 *   자식예외처리()가 호출된다.
 * </pre>
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/v2/")
public class ApiExceptionV2Controller {

  /**
   해당 Controller 내에서 Exception 터졌을 경우, 내부 로직을 실행한다.
   ExceptionHandlerExceptionResolver 를 통하여, Controller 내에 해당 Annotation 있는지 확인하고,
   존재할 경우, 아래 내요을 실행해준다.
   Servlet Container까지 올라가지않고, 아래 로직에서 모두 끝내버린다.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public ErrorResult illegalArgsExHandler(IllegalArgumentException e) {
    log.error("[exceptionHandler] ex", e);
    return new ErrorResult("Bad", e.getMessage());

  }


  @ExceptionHandler
  public ResponseEntity<ErrorResult> customExceptionHandler(CustomException e) {
    log.error("[exceptionHandler] ex", e);
    ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
    return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
  }



  /**
   * @Exception 놓친 Exception이 있다면, 결국 이녀석이 호출된다.
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler
  public ErrorResult exHandler(Exception e) {
    log.error("[exceptionHandler] ex", e);
    return new ErrorResult("EX", "INTERNAL ERROR");
  }


  @GetMapping("members/{id}")
  public MemberDto getMember(@PathVariable("id") String id) {

    if (id.equals("ex")) {
      throw new RuntimeException("Invalid member!");
    }

    if (id.equals("400")) {
      throw new IllegalArgumentException("Invalid argument!");

    }

    if (id.equals("custom-ex")) {
      throw new CustomException("Custom Exception!");
    }

    return new MemberDto(id, "hello-" + id);

  }


  @GetMapping("response-status-ex1")
  public String responseStatusEx1() {
    throw new CustomBadRequestException();
  }

  @GetMapping("response-status-ex2")
  public String responseStatusEx2() {

    // 상태코드와 Error Message 한번에 처리할 수 있는 Exception
    // + messages.properties 사용하여, Error Message 전달할 수 있음
    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad2", new IllegalArgumentException());
  }

  @GetMapping("def-handler-ex")
  public String defaultHandlerException(@RequestParam Integer data) {
    return "ok!!!";
  }


  @Data
  @AllArgsConstructor
  private class MemberDto {
    private String memberId;
    private String name;
  }
}
