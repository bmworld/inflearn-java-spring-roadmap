package hello.aop.internalCall;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <h1>Proxy와 내부호출 대안: 구조변경</h1>
 * <pre>
 *   애초에 순환참조가 발생할 여지가 발생하지 않도록, 구조를 변경한다.
 * </pre>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CallServiceV3 {
  private final CallServiceV3_InternalService internalService;

  public void externalCall() {
    log.info("Called externally by Proxy 내부호출 대안적용 V3 (구조 변경) / class={}", internalService.getClass());
    internalService.internalCall(); // 자기자신인 객체의 Proxy를 Setter로 주입받아서 사용하여, Proxy내부호출 한계를 극복
  }

}
