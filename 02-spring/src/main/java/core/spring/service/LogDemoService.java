package core.spring.service;

import core.spring.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService {

//  private final ObjectProvider<MyLogger> loggerProvider; // Provider Ver.

  private final MyLogger logger; // Proxy Ver. ==>  @Scope(value="request", proxyMode = ScopedProxyMode.TARGET_CLASS)

  public void logic(String id) {
//    MyLogger logger = loggerProvider.getObject();
    logger.log("service id = "+ id);
  }
}
