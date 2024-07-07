package hello.selector;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class HelloImportSelector implements ImportSelector {

  /** 설정정보를 동적선택할 수 있게 해주는 Interface 구현. */
  @Override
  public String[] selectImports(AnnotationMetadata importingClassMetadata) {
    return new String[] {"hello.selector.HelloConfig"};
    // 프로그래밍 조건문을 사용해서, 해당 문자를 변경하면 됨!
    // ㄴ 핵심: 프로그래밍 코드를 사용한 동적 import !!!
  }
}
