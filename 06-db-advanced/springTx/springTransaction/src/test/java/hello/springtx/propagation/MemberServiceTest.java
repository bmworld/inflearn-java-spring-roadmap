package hello.springtx.propagation;

import hello.springtx.propagation.domain.log.LogMessageExceptionName;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.UnexpectedRollbackException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class MemberServiceTest {

  @Autowired
  MemberService memberService;

  @Autowired
  private MemberRepository memberRepository;

  @Autowired
  private LogRepository logRepository;


  /**
   * @memberService    @Transactional: OFF
   * @MemberRepository @Transactional: ON
   * @LogRepository    @Transactional: ON
   */
  @Test
  void outerTxOff_success() {
    // Given
    String userName = "outerTxOff_success" + geUniqueSuffix();

    // When
    memberService.joinV1(userName);

    // Then: 모든 데이터 정상 저장.
    assertTrue(memberRepository.findByUsername(userName).isPresent());
    assertTrue(logRepository.findByMessage(userName).isPresent());

  }


  /**
   * @memberService    @Transactional: OFF
   * @MemberRepository @Transactional: ON
   * @LogRepository    @Transactional: ON + Exception
   */
  @Test
  void outerTxOff_fail() {
    // Given
    String userName = LogMessageExceptionName.runtimeEx + geUniqueSuffix();

    // When
    Assertions.assertThatThrownBy(()->memberService.joinV1(userName))
      .isInstanceOf(RuntimeException.class);


    // Then: 모든 데이터 정상 저장.
    assertTrue(memberRepository.findByUsername(userName).isPresent()); // Con1 => Commit
    assertTrue(logRepository.findByMessage(userName).isEmpty()); // Con2 =>  Exception 발생하여, Rollback 됨.

  }

  // =================================================================

  /**
   * @memberService    @Transactional: ON
   * @MemberRepository @Transactional: OFF
   * @LogRepository    @Transactional: OFF
   */
  @Test
  void singleTx_success() {
    // Given
    String userName = "outerTxOff_success" + geUniqueSuffix();

    // When
    memberService.joinV1(userName);

    // Then: 모든 데이터 정상 저장.
    assertTrue(memberRepository.findByUsername(userName).isPresent());
    assertTrue(logRepository.findByMessage(userName).isPresent());

  }




  // =================================================================

  /**
   * @memberService    @Transactional: ON
   * @MemberRepository @Transactional: ON
   * @LogRepository    @Transactional: ON
   */
  @Test
  void outerTxOn_success() {
    // Given
    String userName = "outerTx_success" + geUniqueSuffix();

    // When
    memberService.joinV1(userName);

    // Then: 모든 데이터 정상 저장.
    assertTrue(memberRepository.findByUsername(userName).isPresent());
    assertTrue(logRepository.findByMessage(userName).isPresent());

  }




  // =================================================================

  /**
   * @memberService    @Transactional: ON
   * @MemberRepository @Transactional: ON
   * @LogRepository    @Transactional: ON + Exception
   */
  @Test
  void outerTxOn_fail() {
    // Given
    String userName = LogMessageExceptionName.runtimeEx + geUniqueSuffix();

    // When
    Assertions.assertThatThrownBy(()->memberService.joinV1(userName))
      .isInstanceOf(RuntimeException.class);


    // Then: 모든 데이터 정상 저장.
    assertTrue(memberRepository.findByUsername(userName).isEmpty()); // Con1 => Commit
    assertTrue(logRepository.findByMessage(userName).isEmpty()); // Con2 =>  Exception 발생하여, Rollback 됨.

  }


  // =================================================================

  /**
   * @memberService    @Transactional: ON
   * @MemberRepository @Transactional: ON
   * @LogRepository    @Transactional: ON + Exception
   *
   * 두 논리 Tx 중, 하나의 Tx 가 Exception 발생했음에도, Commit 시도할 경우, `try catch 또는 throws 사용하여, Ex 잡았더라도`, `정상 흐름 처리 X`
   * 결과:
   *  1) 두 논리 Tx 모두, rollback
   *    => why? 내부 Tx에서 Ex 발생 시, `rollback-only=true` 상태 추가
   *    => 물리 Tx 종료 시, rollback-only 옵션이 있는 경우, 물리 Tx는 Rollback 된다.
   *    => rollback-only= true 상태에서 Commit 시도할 경우,UnexpectedRollbackException 발생.
   *  2) UnexpectedRollbackException
   *
   */
  @Test
  void recoverException_fail() {
    // Given
    String userName = LogMessageExceptionName.runtimeEx + geUniqueSuffix();

    // When
    Assertions.assertThatThrownBy(()->memberService.joinV2(userName))
      .isInstanceOf(UnexpectedRollbackException.class);


    // Then: 모든 데이터 정상 저장.
    assertTrue(memberRepository.findByUsername(userName).isEmpty()); // Con1 => Commit
    assertTrue(logRepository.findByMessage(userName).isEmpty()); // Con2 =>  Exception 발생하여, Rollback 됨.

  }


  // =================================================================

  /**
   * @memberService    @Transactional: ON
   * @MemberRepository @Transactional: ON
   * @LogRepository    @Transactional: ON(`REQUIRES_NEW`) + Exception
   *
   * REQUIRED_NEW 옵션을 사용하여, 두 논리 Tx를 두 물리 Tx로 분리한다.
   * => 따라서, 어느 하나의 Tx 가 Exception 발생하더라도,
   * 나머지 Tx의 처리는 독립적으로 Rollback 또는 Commit 처리할 수 있다.
   *
   */
  @Test
  void recoverException_success_byREQUIRED_NEW() {
    // Given
    String userName = LogMessageExceptionName.runtimeEx + geUniqueSuffix();

    // When
    memberService.joinWithREQUIRED_NEW_inLogRepo(userName);


    // Then: 모든 데이터 정상 저장.
    assertTrue(memberRepository.findByUsername(userName).isPresent()); // Con1 => Commit
    assertTrue(logRepository.findByMessage(userName).isEmpty()); // Con2 =>  Exception 발생하여, Rollback 됨.

  }

  private static LocalDateTime geUniqueSuffix() {
    return LocalDateTime.now();
  }

}
