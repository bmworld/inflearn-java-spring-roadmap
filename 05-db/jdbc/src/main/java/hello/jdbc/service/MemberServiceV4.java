package hello.jdbc.service;

import hello.jdbc.Repository.MemberRepository;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


/**
 * <h1>MemberRepository Interface 의존 Ver.</h1>
 * <pre>
 *   - Exception 누수 문제 해결
 *   - SQL Exception 제거 (Checked Exception -> Unchecked Exception)
 * </pre>
 */
@Slf4j
public class MemberServiceV4 {


  private final MemberRepository memberRepository;


  public MemberServiceV4(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;


  }


  @Transactional
  public void accountTransfer(String fromId, String toId, int money) {
    bizLogic(fromId, toId, money);
  }

  private void bizLogic(String fromId, String toId, int money) {
    Member fromMember = memberRepository.findById(fromId);
    Member toMember = memberRepository.findById(toId);

    int fromMoney = fromMember.getMoney() - money;
    int toMoney = toMember.getMoney() + money;


    // 돈 뺌
    memberRepository.update(fromId, fromMoney);
    // 검증 성공 시, 다음으로 넘어감
    validation(toMember);
    // 돈 추가
    memberRepository.update(toId, toMoney);
  }

  private static void validation(Member toMember) {
    if (toMember.getMemberId().equals("ex")) {
      throw new IllegalStateException("계좌 이체 중 예외 발생");
    }
  }
}
