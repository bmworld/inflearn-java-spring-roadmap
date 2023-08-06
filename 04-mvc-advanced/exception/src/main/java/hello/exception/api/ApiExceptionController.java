package hello.exception.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api")
public class ApiExceptionController {


  @GetMapping("members/{id}")
  public MemberDto getMember(@PathVariable("id") String id) {

    if (id.equals("ex")) {
      throw new RuntimeException("Invalid member!");
    }



    return new MemberDto(id, "hello-"+id);

  }

  @Data
  @AllArgsConstructor
  private class MemberDto {
    private String memberId;
    private String name;
  }
}
