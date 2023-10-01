package hello.advanced.trace.logTrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * <h1> 기능 설명 </h1>
 * <pre>
 *   - `FieldLogTrace`는 `HelloTraceV2`와 거의 같은 기능을한다.
 *   다만,`TraceId` 동기화 방법을 '파라미터 -> 필드` 로 변경하였다.
 *     =>`TraceId`는  Parameter 전달이 아닌, `FieldLogTrace`의 필드 인 `TraceHolder`에 저장된다.
 * </pre>
 *
 */
@Slf4j
public class FieldLogTrace implements LogTrace{

  private static final String START_PREFIX = "--->";
  private static final String COMPLETE_PREFIX = "<---";
  private static final String EX_PREFIX = "<--X-";

  private TraceId traceIdHolder; // TraceId 동기화 [!] 동기화, 동시성 이슈 있음.

  @Override
  public TraceStatus begin(String message) {
    TraceId traceId = syncTraceId(); // 내부에서 들고 있는 TraceId를 동기화 하기
    long startTimeMs = System.currentTimeMillis();
    log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);
    return new TraceStatus(traceId, startTimeMs, message);
  }

  private TraceId syncTraceId() {
    if (traceIdHolder == null) {
      traceIdHolder = new TraceId();
    } else {
      traceIdHolder = traceIdHolder.createNextId(); // level increase by 1
    }
    return traceIdHolder;
  }

  @Override
  public void end(TraceStatus status) {
    complete(status, null);
  }

  @Override
  public void exception(TraceStatus status, Exception e) {
    complete(status, e);

  }


  private void complete(TraceStatus status, Exception e) {
    Long stopTimeMs = System.currentTimeMillis();
    long resultTimeMs = stopTimeMs - status.getStartTimeMs(); // 총 소요시간.
    TraceId traceId = status.getTraceId();
    if (e == null) {
      log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs);
    } else {
      log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), status.getMessage(), resultTimeMs, e.toString());
    }

    releaseTraceId(); // level decrease by 1
  }

  private void releaseTraceId() {
    if (traceIdHolder.isFirstLevel()) {
      traceIdHolder = null; // Destroy `traceId`
    } else {
      traceIdHolder = traceIdHolder.createPrevId();
    }
  }

  private static String addSpace(String prefix, int level) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < level; i++) {
      sb.append((i == level - 1) ? "|" + prefix : "|   ");
    }
    return sb.toString();
  }

}
