package hello.proxy.pureProxy.decorator.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RealComponent implements Component{
  @Override
  public String operation() {
    log.info("Operate Real Component!");
    return "This is data!";
  }
}
