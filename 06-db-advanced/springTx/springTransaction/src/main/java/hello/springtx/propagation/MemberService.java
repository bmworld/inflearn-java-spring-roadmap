package hello.springtx.propagation;

import hello.springtx.propagation.domain.Member;
import hello.springtx.propagation.domain.log.Log;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final LogRepository logRepository;


  /**
   *
   * <h2>로그 저장 실패 시, Rollback</h2>
   */
  @Transactional
  public void joinV1(String username) {

    Member member = new Member(username);
    Log logMessage = new Log(username);

    saveMember(member);
    saveLog(logMessage);

  }

  /**
   * <h2>로그 저장 실패 시, Rollback  X</h2>
   */
  public void joinV2(String username) {
    Member member = new Member(username);
    Log logMessage = new Log(username);

    saveMember(member);
    saveLogWithTryCatch(logMessage);

  }


  private void saveMember(Member member) {
    log.info("=== memberRepository 호출 시작");
    memberRepository.save(member);
    log.info("=== memberRepository 호출 종료");
  }

  private void saveLog(Log logMessage) {
    log.info("=== logRepository 호출 시작");
    logRepository.save(logMessage);
    log.info("=== logRepository 호출 종료");
  }

  private void saveLogWithTryCatch(Log logMessage) {
    log.info("=== logRepository 호출 시작 (로그 저장 실패 시, Rollback 미적용)");
    try {
      logRepository.save(logMessage);
    } catch (Exception e) {
      log.info("log 저장에 실패했습니다. logMessage={}", logMessage);
      log.info("정상 흐름 지속함.");
    }

    log.info("=== logRepository 호출 종료");
  }
}
