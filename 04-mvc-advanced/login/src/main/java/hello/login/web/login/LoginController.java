package hello.login.web.login;

import hello.login.domain.member.Member;
import hello.login.service.LoginService;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import hello.login.web.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
  /**
   * <h1>Cookie 사용한 로그인 처리</h1>
   */
//  @PostMapping("/login")

  public String loginByCookie(@ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse res) {
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



  /**
   * <h1>DIY Session</h1>
   * <pre>SessionManager 를 통해 Session 생성 & 회원 데이터 보관</pre>
   */
//  @PostMapping("/login")
  public String loginByDIYSession(@ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletResponse res) {
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


  /**
   * <h1>Spring Session</h1>
   * <pre>
   *   - 세션이 존재할 경우, 기존 세션 반환 / 세션 없을 경우, 신규 세션 반환
   * </pre>
   * <h2>Session create 옵션</h2>
   * <pre>
   *   1. `request.getSession(true)` (DEFAULT)
   *     - 세션이 있으면 기존 세션 반환
   *     - 세션이 없으면, 새로운 세션 생성 후 반환
   *
   *   2. `request.getSession(false)`
   *     - 세션이 있으면 기존 세션 반환
   *     - 세션이 없으면 새로운 세션을 생성하지 않음. (null 반환)
   *
   * </pre>
   */
  @PostMapping("/login")
  public String loginBySpringSession(@ModelAttribute LoginForm form, BindingResult bindingResult, HttpServletRequest req) {
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


    // Spring Session 사용
    HttpSession session = req.getSession();
    // 세션 회원 정보 보관
    session.setAttribute(SessionConst.LOGIN_MEMBER, member);

    return "redirect:/";
  }





  // ================================= LOGOUT =================================

  /**
   * <h1>Cookie 사용한 Logout</h1>
   */
//  @PostMapping("/logout")
  public String logoutByCookie(HttpServletResponse res) {
    Cookie cookie = CookieUtils.expireCookie("memberId");
    res.addCookie(cookie);
    return "redirect:/";
  }


  /**
   * <h1>DIY Session 사용한 Logout</h1>
   */
//  @PostMapping("/logout")
  public String logoutByDIYSession(HttpServletRequest req) {
    sessionManager.expireSession(req);
    return "redirect:/";
  }


  /**
   * <h1>Spring Session을 사용한 Logout</h1>
   * <pre>
   *   쿠키는 즉시 삭제되지는 않는다.
   *   어차피, 해당 세션 ID를 쿠키로 다시 전송해도,
   *   서버에서는 이미 만료된 상태라, 다시 쓸수 없기 때문이다.
   * </pre>
   * */
  @PostMapping("/logout")
  public String logoutBySpringSession(HttpServletRequest req) {
    // 세션을 삭제할 것이기 때문에,  false 옵션을 사용해야함. (true일 경우, 세션이 없으면 신규 생성한다.)
    boolean create = false;
    HttpSession session = req.getSession(create);
    if (session != null) {
      session.invalidate(); // 세션과 그 내부 데이터를 모두 삭제한다
    }
    return "redirect:/";
  }
}
