package hello.config;

import memory.MemoryController;
import memory.MemoryFinder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @Conditional(MemoryCondition.class) // 해당 Condition 이 True 인 경우에만 아래 Configuration 이 Bean 등록됨.
// `@ConditionalOnProperty`: 특정조건이 만족할 때 실행함
// `name` = 환경정보의 property 이름 / havingValue = 해당 name 값의 value가 지정한
// 값인지.
// * 환경정보: application.yml (memory=on) 또는 IDE - Configuration - VM Option (`-Dmemory`=on)
@ConditionalOnProperty(name = "memory", havingValue = "on")
public class MemoryConfig {

  @Bean
  public MemoryController memoryController() {
    return new MemoryController(memoryFinder());
  }

  @Bean
  public MemoryFinder memoryFinder() {
    return new MemoryFinder();
  }
}
