package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
@Slf4j
public class SessionInfoController {


  @GetMapping("/session-info")
  public String sessionInfo(HttpServletRequest req) {
    HttpSession session = req.getSession(false);
    if (session == null) {
      return "세션이 없습니다";
    }

    // 세션데이터출력
    session.getAttributeNames().asIterator()
      .forEachRemaining((name)-> log.info("session name={}, value={}", name, session.getAttribute(name)));


    // 세션 고유값
    log.info("session.getId={}", session.getId());
    // 세션 최대 허용시간 (초)
    log.info("session.getMaxInactiveInterval={}", session.getMaxInactiveInterval());
    // 세션 생셩 시점
    log.info("session.getCreationTime={}", new Date(session.getCreationTime()));
    // 신규 생성 여부 [기존 세션: 클라에서 서버로 sessionId(JSESSIONID)를 사용해서 조회한 경우]
    log.info("session.isNew={}", session.isNew());


    // 세션에 마지막으로 접근한 시간
    // 동일 세션에 전달하는 HTTP 요청이 있는 경우, 현재 시간으로 다시 초기화된다
    // 위 조건으로 초기화 되면 `server.servlet.session.timeout={second}` 설정 시간동안 세션을 추가적으로 사용할 수 있다.
    // `LastAccessedTime` 이후로 timeout 시간 초과 시, WAS가 내부에서 해당 세션 제거함
    log.info("session.getLastAccessedTime={}", new Date(session.getLastAccessedTime()));

    return "세션 정보 출력";
  }
}
