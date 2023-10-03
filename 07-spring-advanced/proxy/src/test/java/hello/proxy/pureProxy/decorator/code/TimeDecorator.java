package hello.proxy.pureProxy.decorator.code;

import lombok.extern.slf4j.Slf4j;


/**
 * <h1>Decorator Pattern</h1>
 * <pre>
 *   - 원래 서버가 제공하는 기능에 대헛 부가 기능을 수행한다.
 *   ex)
 *    요청값이나, 응답 값을 중간에 변형
 *    실행 시간을 측정해서 추가 로그를 남김
 *
 *  - Practice: 데코레이터 패턴을 적용하여, 메시지 Decorator 만들기
 *  - Runtime Dependencies: Client -> Message Decorator -> realComponent
 * </pre>
 * <h1>Decorator Example: 실행시간 측정 </h1>
 */
@Slf4j
public class TimeDecorator implements Component{
  public TimeDecorator(Component component) {
    this.component = component;
  }

  private Component component;

  @Override
  public String operation() {
    log.info("Operate TimeDecorator");
    long startTime = System.currentTimeMillis();
    String result = component.operation();
    long endTime = System.currentTimeMillis();
    long workingTime = endTime - startTime;
    log.info("TimeDecorator 종료 > workingTime = {}ms", workingTime);
    return result;
  }
}
