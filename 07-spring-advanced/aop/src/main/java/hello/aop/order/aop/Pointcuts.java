package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

  // hello.aop.order 패키지와 하위 패키지를 모두 가져옴
  @Pointcut("execution(* hello.aop.order..*(..))")
  public void allOrder() {} // pointcut signature -> 여러 AOP에서 사용할 수 있다

  // Class 이름 패턴이 *Service 인것에 적용할 것이다.
  @Pointcut("execution(* *..*Service.*(..))")
  public void allService() {}

  @Pointcut("allOrder() && allService()")
  public void orderAndService() {}

}
