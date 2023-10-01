package hello.advanced.trace.stratege;

import hello.advanced.trace.stratege.code.ContextV1;
import hello.advanced.trace.stratege.code.Strategy;
import hello.advanced.trace.stratege.code.StrategyLogic1;
import hello.advanced.trace.stratege.code.StrategyLogic2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * <h1>`Context` & `Strategy` 선 조립 후 실행 Ver.</h1>
 */
@Slf4j
public class ContextV1Test {

  @Test
  @DisplayName("전략패턴 - 미적용 Ver.")
  public void strategyV0() {
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


  @Test
  @DisplayName("전략패턴 적용 예제")
  public void strategyV1() {
    StrategyLogic1 strategyLogic1 = new StrategyLogic1();
    ContextV1 contextV1 = new ContextV1(strategyLogic1); // 전략패턴: 로직 주입
    contextV1.execute();



    StrategyLogic2 strategyLogic2 = new StrategyLogic2();
    ContextV1 contextV2 = new ContextV1(strategyLogic2); // 전략패턴: 로직 주입
    contextV2.execute();

  }

  @Test
  @DisplayName("전략패턴 적용 예제 - 익명 내부 Class 사용")
  public void strategyV2() {

    Strategy strategyLogic1 = new Strategy() {
      @Override
      public void call() {
        log.info("BIZ LOGIC 1 by ANONYMOUS INNER CLASS");
      }
    };
    ContextV1 contextV1 = new ContextV1(strategyLogic1); // 전략패턴: 로직 주입


    Strategy strategyLogic2 = new Strategy() {
      @Override
      public void call() {
        log.info("BIZ LOGIC 2 by ANONYMOUS INNER CLASS");
      }
    };
    ContextV1 contextV2 = new ContextV1(strategyLogic2); // 전략패턴: 로직 주입
    log.info("strategyLogic2 = {}", strategyLogic2.getClass()); // class => ContextStrategyV1Test$2
    contextV2.execute();

  }


  @Test
  @DisplayName("전략패턴 적용 예제 - 익명 내부 Class 사용")
  public void strategyV3() {

    ContextV1 contextV1 = new ContextV1(new Strategy() {
      @Override
      public void call() {
        log.info("BIZ LOGIC 1 by ANONYMOUS INNER CLASS");
      }
    }); // 전략패턴: 로직 주입


    log.info("strategyLogic1 = {}", contextV1.getClass()); // class => ContextStrategyV1Test$1
    contextV1.execute();



    Strategy strategyLogic2 = new Strategy() {
      @Override
      public void call() {
        log.info("BIZ LOGIC 2 by ANONYMOUS INNER CLASS");
      }
    };
    ContextV1 contextV2 = new ContextV1(strategyLogic2); // 전략패턴: 로직 주입
    log.info("strategyLogic2 = {}", contextV2.getClass()); // class => ContextStrategyV1Test$2
    contextV2.execute();

  }



  @Test
  @DisplayName("전략패턴 적용 예제 - 익명 내부 Class -> lambda 변경(단, interface 내의 메서드가 1개만 존재해야함)")
  public void strategyV4() {

    ContextV1 contextV1 = new ContextV1(() -> log.info("BIZ LOGIC 1 by ANONYMOUS INNER CLASS")); // 전략패턴: 로직 주입
    log.info("strategyLogic1 = {}", contextV1.getClass()); // class => ContextStrategyV1Test$1
    contextV1.execute();


    ContextV1 contextV2 = new ContextV1(() -> log.info("BIZ LOGIC 2 by ANONYMOUS INNER CLASS")); // 전략패턴: 로직 주입
    log.info("strategyLogic1 = {}", contextV2.getClass()); // class => ContextStrategyV1Test$1
    contextV2.execute();
  }


}
