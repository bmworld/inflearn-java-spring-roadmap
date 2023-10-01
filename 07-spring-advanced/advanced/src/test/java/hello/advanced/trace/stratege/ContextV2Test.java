package hello.advanced.trace.stratege;

import hello.advanced.trace.stratege.code.ContextV2;
import hello.advanced.trace.stratege.code.Strategy;
import hello.advanced.trace.stratege.code.StrategyLogic1;
import hello.advanced.trace.stratege.code.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * <h1>`Context` 실행 시, `Strategy` Args 전달 Ver.</h1>
 * <pre>
 *   - Client는 `Context`를 실행하는 시점에 원하는 `Strategy`를 전달 가능
 *   - `Context & Strategy 선 조립 후 실행` 방식과 비교하여
 *     원하는 전략을 더욱 유연하게 변경 가능.
 * </pre>
 */
@Slf4j
public class ContextV2Test {


  /**
   * 전략 패턴 적용
   */
  @Test
  @DisplayName("전략패턴 적용 예제2 - Context 실행 시점에 전략을 Args 로 전달")
  public void strategyV1() {
    ContextV2 contextV1 = new ContextV2();
    contextV1.execute(new StrategyLogic1());

    ContextV2 contextV2 = new ContextV2();
    contextV2.execute(new StrategyLogic2());

  }


  /**
   * 전략 패턴 익명 Class Ver.
   */
  @Test
  @DisplayName("전략패턴 적용 예제2 - 익명 내부 Class 사용 ver.")
  public void strategyV2() {
    ContextV2 contextV1 = new ContextV2();
    contextV1.execute(new Strategy() {
      @Override
      public void call() {
        log.info("BIZ LOGIC 1 via ANONYMOUS INNER CLASS");
      }
    });

    ContextV2 contextV2 = new ContextV2();
    contextV2.execute(new Strategy() {
      @Override
      public void call() {
        log.info("BIZ LOGIC 2 via ANONYMOUS INNER CLASS");
      }
    });

  }



  @Test
  @DisplayName("전략패턴 적용 예제2 - Lambda ver.")
  public void strategyV3() {
    ContextV2 contextV1 = new ContextV2();
    contextV1.execute(() -> log.info("BIZ LOGIC 1 via Lambda Ver."));

    ContextV2 contextV2 = new ContextV2();
    contextV2.execute(() -> log.info("BIZ LOGIC 2 via Lambda Ver."));

  }

}
