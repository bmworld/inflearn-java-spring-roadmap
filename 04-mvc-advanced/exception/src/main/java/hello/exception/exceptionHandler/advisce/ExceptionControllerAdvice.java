package hello.exception.exceptionHandler.advisce;

import hello.exception.exception.CustomException;
import hello.exception.exceptionHandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <h1>@RestControllerAdvice</h1>
 * <pre>
 *   - `@ExceptionHandler`, `@InitBinder` 기능을 모아서 관리 가능
 *   - 대상 컨트롤러 지정 가능
 *     1. 특정 Annotation이 있는 컨트롤러
 *     2. 특정 패키지 지정
 *     3. 특정 클래스 지정
 *     4. 대상 생략 시, 모든 컨트롤러 적용(GLOBAL)
 * </pre>
 *
 */
@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

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


}
