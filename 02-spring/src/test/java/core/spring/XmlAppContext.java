package core.spring;

import core.spring.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class XmlAppContext {

  /**
   * xml 파일은 resource 경로 내에 만드시라.
   */
  @Test
  @DisplayName("XML을 사용한 App Configuration")
  public void xmlAppContext() {
    // xml파일 경로 = src.main.resources.appConfig.xml
    ApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");
    MemberService memberService = ac.getBean("memberService", MemberService.class);
    assertThat(memberService).isInstanceOf(MemberService.class);
  }
}
