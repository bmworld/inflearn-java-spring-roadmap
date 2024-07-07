package hello.selector;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

public class ImportSelectorTest {
  @Test
  @DisplayName("정적 Configuration 사용 Ver.")
  public void staticConfig() throws Exception {
    AnnotationConfigApplicationContext appCtx =
        new AnnotationConfigApplicationContext(StaticConfig.class);
    HelloBean bean = appCtx.getBean(HelloBean.class);
    assertNotNull(bean);
  }

  @Test
  @DisplayName("동적 Configuration 사용 Ver.")
  public void selectorConfig() throws Exception {
    AnnotationConfigApplicationContext appCtx =
        new AnnotationConfigApplicationContext(SelectorConfig.class);
    HelloBean bean = appCtx.getBean(HelloBean.class);
    assertNotNull(bean);
  }

  /** 정적 Config */
  @Configuration
  @Import(HelloConfig.class)
  public static class StaticConfig {}

  /** 동적 Config */
  @Configuration
  @Import(HelloImportSelector.class)
  public static class SelectorConfig {}
}
