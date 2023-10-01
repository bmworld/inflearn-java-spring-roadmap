package hello.advanced.trace.stratege.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StrategyLogic1 implements Strategy{
  @Override
  public void call() {
    log.info("BIZ LOGIC 1 실행");
  }
}
