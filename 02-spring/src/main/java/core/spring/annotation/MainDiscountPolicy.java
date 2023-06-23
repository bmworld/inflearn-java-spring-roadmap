package core.spring.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.*;

// => @Qualifier 에 붙어있는 Annotation 모두 복붙.
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("mainDiscountPolicy")
public @interface MainDiscountPolicy {
}
