package hello.proxy.pureProxy.proxyPattern.code;


import lombok.extern.slf4j.Slf4j;

/**
 * <h1>Proxy Pattern</h1>
 * <pre>
 *   - 주요 기능: 접근제어
 *   - 기존 대상 코드를 수정하지 않고, 주요기능을 구현하는 것이다.
 *   - example)
 *      1. Cache
 * </pre>
 * <h1>Proxy Pattern 적용 후, Runtime 의존관게</h1>
 * <pre>
 *   의존관계: Client -> proxy -> realSubject
 * </pre>
 */
@Slf4j
public class CacheProxy implements Subject{

  private Subject target;
  private String cacheValue;

  public CacheProxy(Subject target) {
    this.target = target;
  }

  @Override
  public String operation() {
    log.info("--- Call Proxy");
    if (cacheValue == null) {
      cacheValue = target.operation();
    }
    return cacheValue;
  }
}
