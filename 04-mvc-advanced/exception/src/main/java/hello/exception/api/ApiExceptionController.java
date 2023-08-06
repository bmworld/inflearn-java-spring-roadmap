package hello.exception.api;

import hello.exception.exception.CustomBadRequestException;
import hello.exception.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class ApiExceptionController {


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

    return new MemberDto(id, "hello-"+id);

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
