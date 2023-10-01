package hello.advanced.trace.template.code;


import lombok.extern.slf4j.Slf4j;


/**
 * <h1>Template Method Patter</h1>
 * <pre>
 *   - 설명: 다형성을 사용하여, 공통부분( 변하지 않는 부분)과 변하는 부분을 분리하여 사용하는 방
 *   - 공통(변하지 않는) code: 부모 class 내에 변하지 않는 Template code 몰아 넣는다.
 *   - 변하는 code: 자식 Class에 두고 상속 & Overriding 사용하여 별도로  호출(call).
 * </pre>
 *
 */
@Slf4j
public abstract class AbstractTemplate {
  public void execute() {
    log.info("Start Logic");
    long startTime = System.currentTimeMillis();
    // Start Biz Logic
    call();
    // End Biz Logic
    long endTime = System.currentTimeMillis();
    long workingTime = endTime - startTime;

    log.info("End Logic");
    log.info("===> WorkingTime = {}ms", workingTime);

  }

  protected abstract void call();
}
