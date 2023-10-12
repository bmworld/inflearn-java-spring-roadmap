package hello.aop.internalCall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * <h1>Example: Proxy & 내부 호출로 인한 문제</h1>
 */
@Slf4j
@Service
public class CallServiceV0 {
  public void externalCall() {
    log.info("Called externally");
    this.internalCall(); // 내부 메서드를 호출한다. => 이러면, Spring Container에 Proxy가 아닌,
  }

  public void internalCall() {
    log.info("Called internally");
  }
}
