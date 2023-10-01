package hello.advanced.trace.template;

import hello.advanced.trace.template.code.AbstractTemplate;
import hello.advanced.trace.template.code.SubclassLogic1;
import hello.advanced.trace.template.code.SubclassLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class TemplateMethodPatternTest {

  @Test
  @DisplayName("templateMethodV0")
  public void templateMethodV0() throws Exception {
    logic1();
    logic2();

  }

  private void logic1() {
    long startTime = System.currentTimeMillis();
    // Start Biz Logic
    log.info("Start Logic 1");
    // End Biz Logic
    long endTime = System.currentTimeMillis();
    long workingTime = endTime - startTime;

    log.info("End Logic > workingTime ={}", workingTime);

  }


  private void logic2() {
    long startTime = System.currentTimeMillis();
    // Start Biz Logic
    log.info("Start Logic 2");
    // End Biz Logic
    long endTime = System.currentTimeMillis();
    long workingTime = endTime - startTime;

    log.info("End Logic > workingTime ={}", workingTime);

  }


  /**
   * Template Method Pattern 적용
   */
  @Test
  @DisplayName("templateMethodV1")
  public void templateMethodV1() {
    SubclassLogic1 template1 = new SubclassLogic1();
    template1.execute();

    SubclassLogic2 template2 = new SubclassLogic2();
    template2.execute();


  }
  @Test
  @DisplayName("templateMethodV2 - anonymous inner class (익명 내부 클래스)")
  public void templateMethodV2() {
    AbstractTemplate template1 = new AbstractTemplate(){

      @Override
      protected void call() {
        log.info("BIZ LOGIC 실행");
      }
    };

    log.info("익명 내부 클래스 이름 = {}", template1.getClass()); // TemplateMethodPatternTest$1
    template1.execute();


    AbstractTemplate template2 = new AbstractTemplate(){

      @Override
      protected void call() {
        log.info("BIZ LOGIC 2 실행");
      }
    };

    log.info("익명 내부 클래스 이름 = {}", template2.getClass()); // TemplateMethodPatternTest$2
    template2.execute();

  }

}
