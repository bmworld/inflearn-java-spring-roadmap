package hello.aop.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * `Method level`에 붙이는 AOP
 */
@Target(ElementType.METHOD)
public @interface MethodAop {
  String value(); // ex) @MethodAop("value")
}
