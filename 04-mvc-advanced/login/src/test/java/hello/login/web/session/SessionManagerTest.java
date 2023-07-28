package hello.login.web.session;

import hello.login.domain.member.Member;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.Cookie;

import static org.assertj.core.api.Assertions.assertThat;

class SessionManagerTest {

  SessionManager sessionManager = new SessionManager();
  @Test
  @DisplayName("TEST Create, Get , Expire Session ")
  void testSession() {
    MockHttpServletResponse res = new MockHttpServletResponse();

    // 세션 생성
    Member member = new Member();
    sessionManager.createSession(res, member);


    // 요청에 응답 쿠키 저장
    MockHttpServletRequest req = new MockHttpServletRequest();
    req.setCookies(res.getCookies()); // mySessionId= 1231212-12312312-3123


    // 세션 조회
    Object result = sessionManager.getSession(req);
    assertThat(result).isEqualTo(member);

    // 세션 만료
    sessionManager.expireSession(req);

    // 세션 만료검증
    Object expiredSession = sessionManager.getSession(req);
    assertThat(expiredSession).isNull();

  }


}
