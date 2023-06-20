package core.spring.config;

import core.spring.OrderApp;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;


/**
 * <h1>ComponentScan</h1>
 * <pre>
 * => @Component Annotation 붙은 class -> scan -> Spring bean 자동 등록
 * 1. 이때, 자동 등록된 Bean은 개발자 측에서 의존성 주입할 방법이 없다.
 *     그래서, @Autowired를 붙여서, Spring이 자동 주입하게 해준다.
 * 2. Spring Bean 기본 전략: 클래스명을 그대로 사용하되, 맨 앞글자만 소문자로 변경.
 * 3. Spring Bean 직접 지정: @Component("Enter_Custom_Bean_Name")
 * </pre>
 *
 *
 *
 * <h1>Component Scan - 대상</h1>
 * <pre>
 * `@Component`: 컴포넌트 스캔에 사용
 * `@Controller`:  Spring Business Logic에 사용
 * `@Service`: Spring MVC Controller에 사용
 * `@Repository`: Spring Data 접근 계층에 사용
 * `@Configuration`: Spring 설정 정보에 사용
 * </pre>
 *
 * <h1>Annotion 부가 기능 설명</h1>
 * <pre>
 * `@Controller`: Spring MVC Controller로 인식
 * `@Service`: 별도제공 기능 없음 /개발자들이 클래스의 역할(비즈니스 로직)을 인식하게 하기 위함
 * `@Repository`: Spring Data 접근 계층에 사용 / 데이터 계층 예외를 Spring Exception 변환
 * `@Configuration`: Spring Bean이 Singleton으로 유지하도록 별도 처리 해줌
 * </pre>
 */

@Configuration
// @ComponentScan =>  @Component 붙은 Class를 Scan해서 Spring Bean으로 등록함.
@ComponentScan(

  basePackages={"core.spring"}, // @Component 붙은 Class 못찾을 경우, 경로를 설정해준다
  // ====> 나는....new AnnotationConfigApplicationContext(AutoAppConfig.class);에서....@Component 붙은 Class를 못찾길래.. basePackages 설정이 꼭필요함.

  //
  basePackageClasses = OrderApp.class, // 미지정 시 default 값 = 현재 class의 Package 및 하위 Package

  excludeFilters = @ComponentScan.Filter(
    // 강사님이 configuration 예제를 진행하면서, 등록해둔 config을 예제로서 살려둗기 위해, 일부로 filter 시킨 것임
    // @Configuration Annotation 내에는 @Component Annotation 붙어있음
    type = FilterType.ANNOTATION, classes = Configuration.class
  )
)

public class AutoAppConfig {
}
