package hello.login.web.session;

import hello.login.web.util.CookieUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


@Component
public class SessionManager {

  private final Map<String, Object> sessionStore = new ConcurrentHashMap<>();
  private String SESSION_COOKIE_NAME = "mySessionCookie";

  public void createSession(HttpServletResponse res, Object value) {
    // create session id & store value at session
    String sessionId = UUID.randomUUID().toString();
    sessionStore.put(sessionId, value);

    Cookie mySessionCookie = CookieUtils.createCookie(SESSION_COOKIE_NAME, sessionId);
    res.addCookie(mySessionCookie);

  }



  public Object getSession(HttpServletRequest req) {
    Cookie sessionCookie = CookieUtils.findCookie(req, SESSION_COOKIE_NAME);
    if (sessionCookie == null) {
      return null;
    }

    String sessionValue = sessionCookie.getValue();
    System.out.println("--- getSession > sessionValue = " + sessionValue);
    return sessionStore.get(sessionValue);
  }


  public void expireSession(HttpServletRequest req) {
    Cookie sessionCookie = CookieUtils.findCookie(req, SESSION_COOKIE_NAME);
    if(sessionCookie != null) {
      String sessionCookieValue = sessionCookie.getValue();
      System.out.println("--- expireSession > sessionCookieValue = " + sessionCookieValue);
      sessionStore.remove(sessionCookieValue);
    }

  }


}
