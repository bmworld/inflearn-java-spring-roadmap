package hello.advanced.trace.threadLocal.code;

import lombok.extern.slf4j.Slf4j;

import static hello.util.customUtils.sleep;

/**
 * <h1>Thread Local 사용법</h1>
 * <pre>
 *   - 저장: `ThreadLocal.set(...)`
 *   - 조회: `ThreadLocal.get()`
 *   - 제거: `ThreadLocal.remove()`
 * </pre>
 * <h1>Thread Local 주의사항</h1>
 * <pre>
 *   해당 Thread가 ThreadLocal을 모두 사용 후,
 *   `ThreadLocal.remove()` 호출하여,
 *   Thread Local 저장 값을 `제거`해주어야 한다.
 *
 * </pre>
 */
@Slf4j
public class ThreadLocalService {
  private ThreadLocal<String> storedName = new ThreadLocal<>();

  public String logic(String name) {
    log.info("저장 name ={} -> storedName ={}", name, storedName.get());
    storedName.set(name);
    sleep(1000); // 저장에 1초 가량 소요된다고 가정
    log.info("조회 storedName ={}", storedName.get());
    return storedName.get();
  }

}
