package core.spring.scan.filter;

import java.lang.annotation.*;

@Target(ElementType.TYPE) // TYPE:  Class level
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyExcludeComponent {
}
