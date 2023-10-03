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
 * <h1>Decorator: 메시지 꾸미기</h1>
 */
@Slf4j
public class MessageDecorator implements Component{

  private Component component;

  public MessageDecorator(Component component) {
    this.component = component;
  }

  @Override
  public String operation() {
    log.info("Operate MessageDecorator");
    String result = component.operation();
    String decoratedResult = "*****DECO***** " + result + " *****DECO***** ";
    log.info("MessageDecorator 적용 전 = {}, 적용 후 = {}", result, decoratedResult);
    return decoratedResult;
  }
}
