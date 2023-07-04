package com.example.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {


  // @Override 시 protected 메서드 사용할것 (public method 아님)
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    printStartLine(req);
    printHeaders(req);
    printHeaderUtils(req);
    printEtc(req);

  }

  private void printStartLine(HttpServletRequest req) {

    System.out.println("--------- REQUEST-LINE - START --------");
    String method = req.getMethod();
    System.out.println("method = " + method);
    String protocol = req.getProtocol();
    System.out.println("protocol = " + protocol);
    String scheme = req.getScheme();
    System.out.println("scheme = " + scheme);
    StringBuffer requestURL = req.getRequestURL();
    System.out.println("requestURL = " + requestURL);
    String requestURI = req.getRequestURI();
    System.out.println("requestURI = " + requestURI);
    // query
    String queryString = req.getQueryString();
    System.out.println("queryString = " + queryString);
    boolean secure = req.isSecure();
    System.out.println("secure = " + secure);
    System.out.println("--------- REQUEST-LINE - END --------");
  }

  private void printHeaders(HttpServletRequest req) {
    System.out.println("--------- Headers - START --------");

    System.out.println("req.getHeader(\"name\") = " + req.getHeader("name"));
    Enumeration<String> em = req.getHeaderNames();

    // 아래 방법 1, 2 중 하나만 조회 가능 (모든 항목이 조회되었을 경우, 다음 방법은 실행되지 않음)

    // 방법2 (as 문법)
    em.asIterator().forEachRemaining(headerName -> System.out.println("방법2) headerName = " + headerName));

    // 방법1 (as 문법)
    while (em.hasMoreElements()) {
      String headerName = em.nextElement();
      System.out.println("방법1) headerName = " + headerName);
    }


    System.out.println("--------- Headers - END --------");
  }



  private void printHeaderUtils(HttpServletRequest req) {
    System.out.println("--------- Headers - 편의 조회 기능 - START --------");

    System.out.println("[Host 편의 조회]");
    System.out.println("req.getServerName() = " + req.getServerName());
    System.out.println("req.getServerPort() = " + req.getServerPort());
    System.out.println();
    System.out.println("[Accept-Language]");
    req.getLocales().asIterator()
        .forEachRemaining(locale -> System.out.println("req.getLocales().asIterator() > locale = " + locale));
    System.out.println("req.getLocale() = " + req.getLocale());
    System.out.println();

    System.out.println("[cookie 편의 조회]");
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie cookie: cookies){
        System.out.println("cookie name and value => " + cookie.getName() + " = " + cookie.getValue());
      }
    }

    System.out.println();

    System.out.println("[Content 편의 조회]");
    System.out.println("req.getContentType() = " + req.getContentType());
    System.out.println("req.getContentLength() = " + req.getContentLength());
    System.out.println("req.getCharacterEncoding() = " + req.getCharacterEncoding());


    System.out.println("--------- Headers - 편의 조회 기능 - END --------");



  }

  private void printEtc(HttpServletRequest req) {

    System.out.println("--------- 기타 조회 기능 - START --------");

    System.out.println("[REMOTE 정보] ===> 로컬 서버의 Connection 정보.");
    System.out.println("req.getRemoteAddr() = " + req.getRemoteAddr());
    System.out.println("req.getRemoteHost() = " + req.getRemoteHost());
    System.out.println("req.getRemotePort() = " + req.getRemotePort());
    System.out.println("req.getRemoteUser() = " + req.getRemoteUser());

    System.out.println("[LOCAL Server 정보]");
    System.out.println("req.getLocalName() = " + req.getLocalName());
    System.out.println("req.getLocalAddr() = " + req.getLocalAddr());
    System.out.println("req.getLocalPort() = " + req.getLocalPort());

    System.out.println("--------- 기타 조회 기능 - END --------");
  }
}
