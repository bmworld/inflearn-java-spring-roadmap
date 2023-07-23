package hello.itemservice.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


/**
 *
 *  <h1>[주의 사항] -_-...</h1>
 *  <p>- 본인 SYSTEM DEFAULT 언어: English
 *  <pre>
 *    1. application.properties > spring.messages.basename=messages 설정한 경우, messages.properties 파일존재해야함.
 *    2. messages.properties > 시스템 기본 언어(본인: en)에 해당하는 properties OVERRIDE 된다.
 *    <h1>[OVERRIDE] messages.properties => messages_en.properties </h1>
 *
 *  </pre>
 *
 */
@SpringBootTest
public class MessageSourceTest {

  @Autowired
  private MessageSource ms;

  @Test
  public void helloMessage() {

    String result = ms.getMessage("hello", null, null); // Locale 넣지않으면 default 또는 System Default language 값 가져옴
    System.out.println("result = " + result);
    assertThat(result).isEqualTo("hello");
  }

  @Test
  public void notFoundMessageCode() {
    assertThatThrownBy(() -> ms.getMessage("no_code", null, null))
      .isInstanceOf(NoSuchMessageException.class);
  }

  @Test
  public void notFoundMessageCodeDefaultMessage() {
    String defaultMessage = "기본 메시지";
    String result = ms.getMessage("no_code", null, defaultMessage, null);
    assertThat(result).isEqualTo(defaultMessage);
  }

  @Test
  public void argumentMessageByDefaultLang() {
    String message = ms.getMessage("hello.name", new Object[]{"Spring", "JAVA"}, null);
    System.out.println("message = " + message);
    assertThat(message).isEqualTo("hello Spring JAVA");
  }

  @Test
  public void defaultLang() {
    String defaultLangResult = ms.getMessage("hello", null, null);
    System.out.println("defaultLangResult = " + defaultLangResult);
    assertThat(defaultLangResult).isEqualTo("hello");
  }

  @Test
  public void enLang() {
    String result = ms.getMessage("hello", null, Locale.ENGLISH);
    System.out.println("result = " + result);

    assertThat(result).isEqualTo("hello");
  }


  @Test
  public void koLang() {
    String result = ms.getMessage("hello", null, Locale.KOREAN);
    System.out.println("result = " + result);

    assertThat(result).isEqualTo("안녕");
  }
}
