package hello.aop.member.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)

/**
 * `Class level`에 붙이는 AOP
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ClassAop {
}
