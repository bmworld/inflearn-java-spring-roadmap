package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.repository.MemberRepository;
import hello.login.web.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;


    //    @GetMapping("/")
    public String home() {
        return "home";
    }


    @GetMapping("/")
    public String homeByLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {
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

    @PostMapping("/logout")
    public String logout(HttpServletResponse res) {
        Cookie cookie = CookieUtils.expireCookie("memberId");
        res.addCookie(cookie);
        return "redirect:/";
    }

}
