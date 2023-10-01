package hello.advanced.trace.stratege.code.template;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeLogTemplate {
  public void execute(Callback callback) {
    long startTime = System.currentTimeMillis();
    // Start Biz Logic
    callback.call(); // ! 위임.
    // End Biz Logic
    long endTime = System.currentTimeMillis();
    long workingTime = endTime - startTime;

    log.info("End Logic > workingTime ={}", workingTime);

  }
}
