package core.spring.DI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * <h1>의존성 주입하기 - Field Injection - @Autowired를 Field에 사용</h1>
 * <pre>
 *   - Dependency를 필드에 바로 주입하는 방법
 *   - *** TEST하기 힘든 방법 (why? DI Framework 없으면, 아무 것도 할 수 없음)
 * </pre>
 * <h1>Field Injection은 TEST 코드에서만 사용하자. </h1>
 */
@Component
public class AutowiredAtField {

  @Autowired
  private AutowiredAtFieldTarget autowiredAtFieldTarget;

  private AutowiredAtFieldNonTarget autowiredAtFieldNonTarget;


  public AutowiredAtFieldTarget getAutowiredAtFieldTarget() {
    return autowiredAtFieldTarget;
  }

  public AutowiredAtFieldNonTarget getAutowiredAtFieldNonTarget() {
    return autowiredAtFieldNonTarget;
  }


}
