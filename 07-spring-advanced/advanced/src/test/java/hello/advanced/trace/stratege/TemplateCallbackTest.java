package hello.advanced.trace.stratege;

import hello.advanced.trace.stratege.code.template.Callback;
import hello.advanced.trace.stratege.code.template.TimeLogTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateCallbackTest {

  /**
   * Template callback Pattern
   */
  @Test
  @DisplayName("Template Callback Pattern - Basic")
  public void callbackV1() {

    TimeLogTemplate template = new TimeLogTemplate();
    template.execute(new Callback(){

      @Override
      public void call() {
        log.info("BIZ LOGIC 1");
      }
    });

    template.execute(new Callback(){

      @Override
      public void call() {
        log.info("BIZ LOGIC 2");
      }
    });

  }


  @Test
  @DisplayName("Template Callback Pattern - with Lambda")
  public void callbackV2() {

    TimeLogTemplate template = new TimeLogTemplate();
    template.execute(() -> log.info("BIZ LOGIC 1"));
    template.execute(() -> log.info("BIZ LOGIC 2"));
  }

}
