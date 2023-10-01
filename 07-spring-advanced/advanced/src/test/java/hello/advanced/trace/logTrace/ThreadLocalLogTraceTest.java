package hello.advanced.trace.logTrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


class ThreadLocalLogTraceTest {

  ThreadLocalLogTrace trace = new ThreadLocalLogTrace();

  @Test
  @DisplayName("begin_end_level2")
  public void begin_end_level2() {
    TraceStatus status1 = trace.begin("message1");
    TraceStatus status2 = trace.begin("message2");
    trace.end(status2);
    trace.end(status1);
  }

  @Test
  @DisplayName("begin_end_level3")
  public void begin_end_level3() {
    TraceStatus status1 = trace.begin("message1");
    TraceStatus status2 = trace.begin("message2");
    TraceStatus status3 = trace.begin("message3");

    trace.end(status3);
    trace.end(status2);
    trace.end(status1);
  }


  @Test
  @DisplayName("begin_exception_level2")
  public void begin_exception_level2() {
    TraceStatus status1 = trace.begin("message1");
    TraceStatus status2 = trace.begin("message2");

    trace.exception(status2, new IllegalStateException("TEST Exception"));
    trace.exception(status1, new IllegalStateException("TEST Exception2"));
  }

}
