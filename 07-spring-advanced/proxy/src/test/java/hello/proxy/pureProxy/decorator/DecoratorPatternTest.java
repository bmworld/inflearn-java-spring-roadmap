package hello.proxy.pureProxy.decorator;

import hello.proxy.pureProxy.decorator.code.DecoratorPatterClient;
import hello.proxy.pureProxy.decorator.code.MessageDecorator;
import hello.proxy.pureProxy.decorator.code.RealComponent;
import hello.proxy.pureProxy.decorator.code.TimeDecorator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@Slf4j
public class DecoratorPatternTest {
  @DisplayName("no Decorator")
  @Test
  void noDecorator() {
    RealComponent realComponent = new RealComponent();
    DecoratorPatterClient client = new DecoratorPatterClient(realComponent);
    client.execute();
  }


  @DisplayName("Decorator ex1: 원본 데이터에 추가적인 메시지 더하기")
  @Test
  void decorator_ex_AddMessage() {
    RealComponent realComponent = new RealComponent();
    MessageDecorator messageDecorator = new MessageDecorator(realComponent);
    DecoratorPatterClient client = new DecoratorPatterClient(messageDecorator);
    client.execute();
  }

  @DisplayName("Decorator ex2: Chaining (여러 개의 Decorator 연결)")
  @Test
  void decorator_ex_multiDecoratorAsChain() {
    RealComponent realComponent = new RealComponent();
    MessageDecorator messageDecorator = new MessageDecorator(realComponent); // Chain 1
    TimeDecorator timeDecorator = new TimeDecorator(messageDecorator); // Chain 2
    DecoratorPatterClient client = new DecoratorPatterClient(timeDecorator);
    client.execute();
  }
}
