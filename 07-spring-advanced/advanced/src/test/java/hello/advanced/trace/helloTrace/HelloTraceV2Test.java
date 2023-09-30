package hello.advanced.trace.helloTrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class HelloTraceV2Test {


  @Test
  @DisplayName("case: begin end")
  public void begin_end() {

    HelloTraceV2 trace = new HelloTraceV2();
    TraceStatus status1 = trace.begin("hello!");
    TraceStatus status2 = trace.beginSync(status1.getTraceId(), status1.getMessage());

    trace.end(status2);
    trace.end(status1);
  }

  @Test
  @DisplayName("case: begin exception")
  public void begin_exception() {

    HelloTraceV2 trace = new HelloTraceV2();
    TraceStatus status1 = trace.begin("helloEx");
    TraceStatus status2 = trace.beginSync(status1.getTraceId(), status1.getMessage());

    trace.exception(status2, new IllegalStateException());
    trace.exception(status1, new IllegalStateException());

  }
}
