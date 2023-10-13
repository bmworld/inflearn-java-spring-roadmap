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
public class CallServiceV1Test {

  @Autowired
  CallServiceV1 callService;
  @Test
  @DisplayName("externalCall: Proxy 내부호출 이슈 해결 (Setter로 자기자신의 Proxy를 주입받아서, 해당 Proxy를 사용하여, 내부 메서드 호출)")
  void externalCall() {
    callService.externalCall();
  }

}
