package hello.login.web.login;

import hello.login.domain.member.Member;
import hello.login.service.LoginService;
import hello.login.web.session.SessionManager;
import hello.login.web.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
@RequiredArgsConstructor
public class LoginController {

  private final LoginService loginService;

  private final SessionManager sessionManager;

  // ================================= GET LOGIN FORM =================================
  @GetMapping("/login")
  public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
    return "login/loginForm";
  }



  // ================================= LOGIN =================================
//  @PostMapping("/login")

//  public String loginV1(@ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse res) {
//    if (bindingResult.hasErrors()) {
//      return "login/loginForm";
//    }
//
//    Member member = loginService.login(form.getLoginId(), form.getPassword());
//    if (member == null) {
//      bindingResult.reject("loginFailure", "아이디 또는 비밀번호를 확인해주세요.");
//      return "login/loginForm";
//    }
//
//    // TODO: 로그인 성공 처리
//    log.info("Login success member={}", member);
//    Cookie cookie = CookieUtils.createCookie("memberId", String.valueOf(member.getId()));
//    res.addCookie(cookie);
//    return "redirect:/";
//  }
//




  /**
   * SessionManager 를 통해 Session 생성 & 회원 데이터 보관
   */
  @PostMapping("/login")
  public String loginV2(@ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse res) {
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
    // 세션 생성 및 세션 데이터 보관
    sessionManager.createSession(res, member);
    return "redirect:/";
  }




  // ================================= LOGOUT =================================
//  @PostMapping("/logout")
//  public String logoutV1(HttpServletResponse res) {
//    Cookie cookie = CookieUtils.expireCookie("memberId");
//    res.addCookie(cookie);
//    return "redirect:/";
//  }

  @PostMapping("/logout")
  public String logoutV2(HttpServletRequest req) {
    sessionManager.expireSession(req);
    return "redirect:/";
  }
}
