package hello.aop.internalCall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


/**
 * <h1>Proxy와 내부호출 대안: 지연 조회</h1>
 */
@Slf4j
@Service
public class CallServiceV2 {

  /**
   * ApplicationContext Ver.
   * -> 이것은 기능이 너무 많아서, 사용하지 않음 => ObjectProvider 로 대체
   */
//  private final ApplicationContext ac;
  //  @Autowired
//  public CallServiceV2(ApplicationContext ac) {
//    this.ac = ac;
//  }
//  public void externalCall() {
//    log.info("Called externally by Proxy 내부호출 대안적용 V2 (지연조회)");
//    CallServiceV2 callServiceV2 = ac.getBean(CallServiceV2.class);
//    callServiceV2.internalCall(); // 자기자신인 객체의 Proxy를 Setter로 주입받아서 사용하여, Proxy내부호출 한계를 극복
//  }

  private final ObjectProvider<CallServiceV2> callServiceProvider;

  public CallServiceV2(ObjectProvider<CallServiceV2> callServiceProvider) {
    this.callServiceProvider = callServiceProvider;
  }
  public void externalCall() {
    log.info("Called externally by Proxy 내부호출 대안적용 V2 (지연조회)");
    CallServiceV2 callServiceV2 = callServiceProvider.getObject();
    callServiceV2.internalCall(); // 자기자신인 객체의 Proxy를 Setter로 주입받아서 사용하여, Proxy내부호출 한계를 극복
  }



  public void internalCall() {
    log.info("Called internally");
  }
}
