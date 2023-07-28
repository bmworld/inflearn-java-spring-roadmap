package hello.login.web.util;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class CookieUtils {
  public static Cookie expireCookie(String cookieName) {
      Cookie cookie = new Cookie(cookieName, null);
      cookie.setMaxAge(0);
      return cookie;
  }

  public static Cookie createCookie(String cookieName, String value) {
    return new Cookie(cookieName, value);
  }

  public static Cookie findCookie(HttpServletRequest req, String cookieName) {
    Cookie[] cookies = req.getCookies();
    if (cookies == null) {
      return null;
    }

    return Arrays.stream(cookies)
      .filter(cookie -> cookie.getName().equals(cookieName))
      .findAny()
      .orElse(null);

  }
}
