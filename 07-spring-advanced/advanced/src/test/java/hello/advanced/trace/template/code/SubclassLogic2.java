package hello.advanced.trace.template.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubclassLogic2 extends AbstractTemplate{
  @Override
  protected void call() {
    log.info("BIZ LOGIC 2 실행");
  }
}
