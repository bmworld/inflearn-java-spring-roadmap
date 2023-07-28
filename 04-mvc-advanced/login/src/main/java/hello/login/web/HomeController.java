package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.repository.MemberRepository;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.*;

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


    // =============================== LOGIN BY DIY SESSION ==================================
//    @GetMapping("/")
    public String homeByLoginByDIYSession(HttpServletRequest req, Model model) {
        // 세션관리자 정보 조회
        Member foundMember = (Member) sessionManager.getSession(req);
        if (foundMember == null) {
            return "home";
        }

        model.addAttribute("member", foundMember);

        return "loginHome";

    }



    // =============================== LOGIN BY Spring SESSION ==================================

    /**
     * <h1>Spring Session</h1>
     */
//    @GetMapping("/")
    public String homeByLoginBySpringSession(HttpServletRequest req, Model model) {
        // 세션관리자 정보 조회
        boolean create = false;
        HttpSession session = req.getSession(create);
        req.getSession(false);

        if (session == null) {
            return "home";
        }


        // 세션 내부에 회원 데이터 없는 경우
        Member foundMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
        if (foundMember == null) {
            return "home";
        }


        model.addAttribute("member", foundMember);

        return "loginHome";

    }


    /**
     * <h1>Session 편의 기능 - @SessionAttribute</h1>
     * <pre>
     *   - Annotation을 사용하여, 세션 찾기 & 세션 데이터 조회를 편리하게 처리해준다.
     *   - required = false: 세션값이 없는 경우 대응
     *   - 해당 Annotation은 "세션 생성" 기능은 없다.
     * </pre>
     */

    @GetMapping("/")
    public String homeByLoginBySpringSessionByAnnotation(@SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member member, Model model) {


        // 회원 정보가 없으면 Redir
        if (member == null) {
            return "home";
        }


        model.addAttribute("member", member);

        return "loginHome";

    }

}
