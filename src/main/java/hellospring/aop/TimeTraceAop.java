package hellospring.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component // * Spring Bean에 등록하는 방법
public class TimeTraceAop {
  @Around("execution(* hellospring..*(..))")
  public Object execute (ProceedingJoinPoint joinPoint) throws Throwable {
    long start = System.currentTimeMillis();
    System.out.println("--------------- START :" + joinPoint.toString());
    try {
      return joinPoint.proceed();
    } finally {
      long finish = System.currentTimeMillis();
      long timeMs = finish - start;
      System.out.println("--------------- END :" + joinPoint.toString() + " " + timeMs + "ms");
    }

  }
}
