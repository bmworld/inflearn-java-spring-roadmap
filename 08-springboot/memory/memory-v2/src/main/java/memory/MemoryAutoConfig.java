package memory;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * springboot 에 자동구성 대상을 지정하는 것도 해줘야한다. 아래 경로에 파일을 만든다.
 * resources/META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports ㄴ
 * spring 실행 시, 위 파일을 읽어서 등록된 Config 파일을 자동등록해준다.
 */
@AutoConfiguration // spring 구성 자동구성 기능 적용 시 사용
@ConditionalOnProperty(
    name = "memory",
    havingValue = "on") // 라이브러리를 가지고 있더라도 상황에 따라 ON/OFF 적용하기 위한 기능 제고
public class MemoryAutoConfig {

  @Bean
  MemoryController memoryController() {

    return new MemoryController(memoryFinder());
  }

  @Bean
  MemoryFinder memoryFinder() {
    return new MemoryFinder();
  }
}
