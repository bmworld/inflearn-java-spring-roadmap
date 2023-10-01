package hello.advanced.trace.stratege.code;


import lombok.extern.slf4j.Slf4j;

/**
 * <h1>전략 패턴: 필드에 '전략'을 보관</h1>
 * <pre>
 *   - `Context`: 변하지 않는 Logic을 가지고 있는 Template 역할을 한다.
 *   - `Strategy`: 변하는 부분을 interface를 만든 후 해당 interface를 구현하는 방식으로 문제 해결
 *
 *
 * </pre>
 * <h1>전략패턴의 핵심</h1>
 *  : `Context`는 `Strategy` interface에만 의존한다.
 *    따라서, Strategy 구현체를 변경거나 새로 생성한다고 하더라도, Context Code에 영향을 끼치지 않음.
 *
 */

@Slf4j
public class ContextV1 {
  private Strategy strategy;

  public ContextV1(Strategy strategy) {
    this.strategy = strategy;
  }

  public void execute() {
    long startTime = System.currentTimeMillis();
    // Start Biz Logic
    strategy.call(); // ! 위임함.
    // End Biz Logic
    long endTime = System.currentTimeMillis();
    long workingTime = endTime - startTime;

    log.info("End Logic > workingTime ={}", workingTime);

  }
}
