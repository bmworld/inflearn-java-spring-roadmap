package hello.thymeleafbasic.basic;

import hello.thymeleafbasic.basic.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/basic")
public class BasicController {

  @GetMapping("/text-basic")
  public String textBasic(Model model) {
    model.addAttribute("data", "Hello <b>Thymeleaf!</b>");
    return "/basic/text-basic";

  }


  @GetMapping("/text-unescaped")
  public String textUnescaped(Model model) {
    model.addAttribute("data", "Hello <b>Thymeleaf!</b>");
    return "/basic/text-unescaped";

  }


  @GetMapping("/variable")
  public String variable(Model model) {

    List<User> users = new ArrayList<User>();
    User userA = new User("usernameA", 10);
    User userB = new User("usernameB", 13);

    users.add(userA);
    users.add(userB);
    Map<String, User> userMap = new HashMap<>();
    userMap.put("userA", userA);
    userMap.put("userB", userB);


    model.addAttribute("user", userA);
    model.addAttribute("users", users);
    model.addAttribute("userMap", userMap);
    return "/basic/variable";

  }

  @GetMapping("/basic-objects")
  public String basicObjects(HttpSession session) {

    session.setAttribute("sessionData", "Hello Session!");
    return "/basic/basic-objects";
  }



  @GetMapping("/date")
  public String date(Model model) {

    model.addAttribute("localDateTime", LocalDateTime.now());
    return "/basic/date";
  }


  @GetMapping("/link")
  public String link(Model model) {
    model.addAttribute("param1", "DATA1");
    model.addAttribute("param2", "DATA2");
    return "/basic/link";
  }

  @GetMapping("/literal")
  public String literal(Model model) {
    return "/basic/literal";
  }


  @GetMapping("/operation")
  public String operation(Model model) {
    model.addAttribute("nullData", null);
    model.addAttribute("data", "Spring!!");
    return "/basic/operation";
  }

  @GetMapping("/attribute")
  public String attribute(Model model) {
    model.addAttribute("nullData", null);
    model.addAttribute("data", "Spring!!");
    return "/basic/attribute";
  }


  @GetMapping("/each")
  public String each(Model model) {
    addUsers(model);
    return "/basic/each";
  }


  private void addUsers(Model model) {

    List<User> list = new ArrayList<User>();
    list.add(new User("user1", 10));
    list.add(new User("user2", 20));
    list.add(new User("user3", 30));
    list.add(new User("user4", 40));
    model.addAttribute("users", list);

  }





  @GetMapping("/condition")
  public String condition(Model model) {
    addUsers(model);
    return "/basic/condition";
  }



  @GetMapping("/comments")
  public String comments(Model model) {
    model.addAttribute("data", "HELLO THYMELEAF!");
    return "/basic/comments";
  }


  @GetMapping("/block")
  public String block(Model model) {
    addUsers(model);
    return "/basic/block";
  }


  @GetMapping("/javascript")
  public String javascript(Model model) {
    addUsers(model);
    model.addAttribute("user", new User("myUser", 16) );
    return "/basic/javascript";
  }


}
