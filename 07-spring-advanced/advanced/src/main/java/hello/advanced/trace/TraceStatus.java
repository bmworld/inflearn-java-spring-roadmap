package hello.advanced.trace;

import lombok.Getter;


/**
 * 로그 시작 시의 상태 정보
 */
@Getter
public class TraceStatus {

  private TraceId traceId; // 내부 Transaction Id ( DB Tx 아님.)

  private Long startTimeMs; // 로그 시작 시간

  private String message; // 로그 시작 시, 사용한 메시지 (로그 종료 시, 이 메시지를 사용해서 출력)


  public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
    this.traceId = traceId;
    this.startTimeMs = startTimeMs;
    this.message = message;
  }
}
