package hello.aop.exam;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class ExamTest {
  @Autowired
  private ExamService examService;

  @Test
  void examTest() {
    int exceptionCount = 5;
    for (int i = 1; i <= exceptionCount; i++) {
      if (i == exceptionCount) {
        String itemId = "itemId-" + i;
        assertThatThrownBy(() -> examService.request(itemId))
          .isInstanceOf(IllegalStateException.class);
      } else {
        examService.request("itemId-" + i);
      }

    }
  }
}
