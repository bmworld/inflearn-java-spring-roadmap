package core.spring.service;

import core.spring.common.MyLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogDemoService {
  private final ObjectProvider<MyLogger> loggerProvider; // request scope => client request가 있어야 존재하는데, 요청이 없으니 최초 주입 시 에러가 발생한다.
  // 따라서 이때, Provider를 사용해야하는 거다.
  public void logic(String id) {
    MyLogger logger = loggerProvider.getObject();
    logger.log("service id = "+ id);
  }
}
