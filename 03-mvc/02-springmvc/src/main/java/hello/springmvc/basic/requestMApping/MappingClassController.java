package hello.springmvc.basic.requestMApping;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/mapping/users")
public class MappingClassController {


  public String user() {
    return "get User";
  }


  public String addUser() {
    return "POST user";
  }

  @GetMapping("/{userId}")
  public String findUser(@PathVariable String userId) {
    return "get userId = " + userId;
  }


  @PatchMapping("/{userId}")
  public String updateUser(@PathVariable String userId) {
    return "update userId = " + userId;
  }


  @DeleteMapping("/{userId}")
  public String deleteUser(@PathVariable String userId) {
    return "delete userId = " + userId;
  }
}
