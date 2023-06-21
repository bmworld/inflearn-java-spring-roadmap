package core.spring.scan.duplication;

import core.spring.OrderApp;
import core.spring.repository.MemberRepository;
import core.spring.repository.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;



// ! 해당 @Bean 주석 해제  => CoreApplicationTests 실행 시, ERROR!!!!
// Invalid bean definition with name 'duplicatedBeanName' defined in class path
@Configuration
@ComponentScan(
  excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Configuration.class)
)
public class DuplicatedAutoAppConfig {
//  @Bean
//  MemberRepository duplicatedBeanName() {
//    System.out.println("DuplicatedAutoAppConfig.memberRepository");
//    return new MemoryMemberRepository();
//  }
}
