package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class ExecutionTest {

  private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
  private Method myMethod;

  @BeforeEach
  public void init() throws NoSuchMethodException {
    myMethod = MemberServiceImpl.class.getMethod("hello", String.class); // (메서드명, 반환타입)

  }

  @Test
  @DisplayName("print method")
  public void printMethod() {
    log.info("myMethod = {}", myMethod); // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
  }
}
