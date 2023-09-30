package hello.advanced.trace.helloTrace;

import hello.advanced.trace.TraceStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootApplication
class HelloTraceV1Test {


  @Test
  @DisplayName("case: begin end")
  public void begin_end() {

    HelloTraceV1 trace = new HelloTraceV1();
    TraceStatus status = trace.begin("hello!");

    trace.end(status);
  }

  @Test
  @DisplayName("case: begin exception")
  public void begin_exception() {

    HelloTraceV1 trace = new HelloTraceV1();
    TraceStatus status = trace.begin("helloEx");
    trace.exception(status, new IllegalStateException());

  }
}
