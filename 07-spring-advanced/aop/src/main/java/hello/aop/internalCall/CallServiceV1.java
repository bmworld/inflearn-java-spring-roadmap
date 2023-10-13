package hello.aop.internalCall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * <h1>Proxy와 내부호출 대안 -> 자기 자신(As Proxy) 을 주입</h1>
 */
@Slf4j
@Service
public class CallServiceV1 {

  private CallServiceV1 callServiceV1;

  /**
   * Spring Bean 등록된 이후, Proxy화된 본인 객체를 주입받는다.
   */
  @Autowired
  public void setCallServiceV1(CallServiceV1 callServiceV1) {
    log.info("callServiceV1 setter = {}", callServiceV1); // hello.aop.internalCall.CallServiceV1@24eb65e3
    this.callServiceV1 = callServiceV1;
  }

  /**
   * ! ERROR CASE: Constructor 주입방식 -> 순환참조 오류 ( Relying upon circular references is discouraged and they are prohibited by default. )
   */
//  @Autowired //
//  public CallServiceV1(CallServiceV1 callServiceV1) {
//    this.callServiceV1 = callServiceV1;
//  }


  public void externalCall() {
    log.info("Called externally by Proxy 내부호출 대안적용 V1");
    callServiceV1.internalCall(); // 자기자신인 객체의 Proxy를 Setter로 주입받아서 사용하여, Proxy내부호출 한계를 극복
  }

  public void internalCall() {
    log.info("Called internally");
  }
}
