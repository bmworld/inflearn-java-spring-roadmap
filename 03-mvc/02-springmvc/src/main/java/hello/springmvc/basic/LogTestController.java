package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Slf4j // <-- [Log 사용방법 / Spring 기본 제공]
public class LogTestController {
//  private final Logger log = LoggerFactory.getLogger(getClass());  // <-- 방법1

  @RequestMapping("/log-test")
  public String logTest() {
    String name = "Spring!";

    System.out.println("name = " + name);

    // ################ LOG 사용 주의 사항 (*** JAVA 언어와 관련있음)
    log.trace("----- trace log=" + name); // log level 에 trace 미포함인 경우에도, 불.필.요.한. `+` 연.산. 발생!!!!

    // LOG LEVEL  ( trace => debug => info => warn => error )
    log.trace("----- trace log={}", name); // 별도 Log 안찍힘 => application.properties 별도 설정 필요
    log.debug("----- debug log={}", name); // debug = 개발 서버용 => application.properties 별도 설정 필요
    log.info("----- info log={}", name); // info= 운영 System에서 확인할 Business 주요 정보
    log.warn("----- warn log={}", name); // warn = 경고를 표시해줘
    log.error("----- error log={}", name); // error = 별도의 알람이나 파일로 확인이 꼭 필요해

    return "OK!";
  }
}
