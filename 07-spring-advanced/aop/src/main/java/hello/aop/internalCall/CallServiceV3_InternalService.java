package hello.aop.internalCall;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class CallServiceV3_InternalService {




  public void internalCall() {
    log.info("Called internally by this class={}", this.getClass());
  }
}
