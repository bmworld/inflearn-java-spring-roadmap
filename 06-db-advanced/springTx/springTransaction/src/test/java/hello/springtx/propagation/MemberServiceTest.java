package hello.springtx.propagation;

import hello.springtx.propagation.domain.log.LogMessageExceptionName;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

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
    String userName = "outerTxOff_success";

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
    String userName = LogMessageExceptionName.runtimeEx;

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
    String userName = "outerTxOff_success";

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

  private static LocalDateTime geUniqueSuffix() {
    return LocalDateTime.now();
  }

}
