package hello.proxy.pureProxy.proxyPattern.code;

import lombok.extern.slf4j.Slf4j;

import static hello.proxy.util.CustomUtils.sleep;

@Slf4j
public class RealSubject implements Subject{
  @Override
  public String operation() {
    log.info("실제 객체 호출");
    sleep(1000); // 데이터 조회에 1초 걸림으로 가정
    return "data!";
  }
}
