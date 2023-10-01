package hello.advanced.trace.stratege.code;


import lombok.extern.slf4j.Slf4j;

/**
 * <h1>전략 패턴: Method에 '전략' 주입 Ver.</h1>
 * <pre>
 *   - `Context`: 변하지 않는 Logic을 가지고 있는 Template 역할을 한다.
 *   - `Strategy`: 변하는 부분을 interface를 만든 후 해당 interface를 구현하는 방식으로 문제 해결
 * </pre>
 */

@Slf4j
public class ContextV2 {
  public void execute(Strategy strategy) {
    long startTime = System.currentTimeMillis();
    // Start Biz Logic
    strategy.call(); // ! 위임함.
    // End Biz Logic
    long endTime = System.currentTimeMillis();
    long workingTime = endTime - startTime;

    log.info("End Logic > workingTime ={}", workingTime);

  }
}
