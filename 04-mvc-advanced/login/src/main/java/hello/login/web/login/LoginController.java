package hello.login.web.login;

import hello.login.domain.member.Member;
import hello.login.service.LoginService;
import hello.login.web.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

  private final LoginService loginService;

  @GetMapping("/login")
  public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
    return "login/loginForm";
  }

  @PostMapping("/login")
  public String login(@ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse res) {
    if (bindingResult.hasErrors()) {
      return "login/loginForm";
    }

    Member member = loginService.login(form.getLoginId(), form.getPassword());
    if (member == null) {
      bindingResult.reject("loginFailure", "아이디 또는 비밀번호를 확인해주세요.");
      return "login/loginForm";
    }

    // TODO: 로그인 성공 처리
    log.info("Login success member={}", member);
    Cookie cookie = CookieUtils.createCookie("memberId", String.valueOf(member.getId()));
    res.addCookie(cookie);
    return "redirect:/";
  }

}
