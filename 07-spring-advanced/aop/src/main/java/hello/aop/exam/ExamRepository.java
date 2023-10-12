package hello.aop.exam;

import hello.aop.exam.annotation.Retry;
import hello.aop.exam.annotation.Trace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class ExamRepository {
  private static int seq = 0;

  /**
   * ex: 5번 중 1회 실패하는 Example
   */
  @Trace
  public String save(String itemId) {
    seq++;
    if (seq % 5 == 0) {
      log.info("[Exception] client request itemId ={}", itemId);
      throw new IllegalStateException("5번 중 1회 발생하는 예외!");
    }
    return "save!";
  }


  @Trace
  @Retry(value = 4)
  public String saveWithRetry(String itemId) {
    seq++;
    if (seq % 5 == 0) {
      log.info("[Exception] client request itemId ={}", itemId);
      throw new IllegalStateException("5번 중 1회 발생하는 예외!");
    }
    return "save with retry!";
  }


  public void reset() {
    seq = 0;
  }
}
