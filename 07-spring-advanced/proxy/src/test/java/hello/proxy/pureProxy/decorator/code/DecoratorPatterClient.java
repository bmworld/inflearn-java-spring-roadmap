package hello.proxy.pureProxy.decorator.code;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DecoratorPatterClient {
  private Component component;

  public DecoratorPatterClient(Component component) {
    this.component = component;
  }

  public void execute() {
    String result = component.operation();
    log.info("DecoratorPatterClient >  execute result = {}", result);
  }


}
