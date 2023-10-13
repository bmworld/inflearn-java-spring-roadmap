package hello.aop.internalCall;

import hello.aop.internalCall.aop.CallAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(CallAspect.class)
public class CallServiceV3Test {

  @Autowired
  CallServiceV3 callService;
  @Test
  @DisplayName("externalCall: Proxy 내부호출 이슈 해결 (내부 호출자체이 발생하지 않도록 구조 변경)")
  void externalCall() {
    callService.externalCall();
  }

}
