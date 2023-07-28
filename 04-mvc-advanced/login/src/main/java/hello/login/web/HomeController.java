package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.repository.MemberRepository;
import hello.login.web.session.SessionManager;
import hello.login.web.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    private final SessionManager sessionManager;

    //    @GetMapping("/")
    public String home() {
        return "home";
    }


    // =============================== LOGIN BY COOKIE ==================================

//    @GetMapping("/")
    public String homeByLoginByCookie(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
        if (memberId == null) {
            return "home";
        }


        Member foundMember = memberRepository.findById(memberId);
        if (foundMember == null) {
            return "home";
        }

        model.addAttribute("member", foundMember);

        return "loginHome";

    }


    // =============================== LOGIN BY SESSION ==================================
    @GetMapping("/")
    public String homeByLoginBySession(HttpServletRequest req, Model model) {
        // 세션관리자 정보 조회
        Member foundMember = (Member) sessionManager.getSession(req);
        if (foundMember == null) {
            return "home";
        }

        model.addAttribute("member", foundMember);

        return "loginHome";

    }

}
