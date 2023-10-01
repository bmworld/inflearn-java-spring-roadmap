package hello.advanced.trace.template;

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

}
