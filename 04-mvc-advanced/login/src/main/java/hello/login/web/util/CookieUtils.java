package hello.login.web.util;


import javax.servlet.http.Cookie;

public class CookieUtils {
  public static Cookie expireCookie(String cookieName) {
      Cookie cookie = new Cookie(cookieName, null);
      cookie.setMaxAge(0);
      return cookie;
  }

  public static Cookie createCookie(String cookieName, String value) {
    return new Cookie(cookieName, value);
  }
}
