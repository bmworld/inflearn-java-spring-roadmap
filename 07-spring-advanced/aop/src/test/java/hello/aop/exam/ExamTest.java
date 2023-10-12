package hello.aop.exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ExamTest {
  @Autowired
  private ExamService examService;
  @Autowired
  private ExamRepository examRepository;

  @BeforeEach
  public void init() {
    examRepository.reset();
  }

  @Test
  void logTest() {
    int exceptionCount = 5;
    for (int i = 1; i <= exceptionCount; i++) {
      String itemId = "itemId-" + i;
      if (i == exceptionCount) {
        assertThatThrownBy(() -> examService.request(itemId))
          .isInstanceOf(IllegalStateException.class);
      } else {
        examService.request(itemId);
      }

    }
  }


  @Test
  void retryTest() {
    int exceptionCount = 5;
    for (int i = 1; i <= exceptionCount; i++) {
      String itemId = "itemId-" + i;
      examService.requestWithRetry(itemId);
    }
  }
}
