package memory;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MemoryFinder {
  public Memory get() {
    long max = Runtime.getRuntime().maxMemory(); // JVM 사용할 수 있는 최대 메모리

    long total = Runtime.getRuntime().totalMemory(); // JVM 확보한 전체 메모리

    long free = Runtime.getRuntime().freeMemory(); // Total 중 사용하지 않은 Memory

    long used = total - free; // JVM 사용 중인 Memory

    return new Memory(used, max);
  }

  @PostConstruct
  public void init() {
    log.info("--- Init memoryFinder");
  }
}
