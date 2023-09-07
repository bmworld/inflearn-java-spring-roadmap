package hello.jdbc.service;

import hello.jdbc.Repository.MemberRepositoryEx;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;


/**
 * <h1>[ BAD CASE ] MemberRepository Interface - Exception 의존 Ver.</h1>
 * <pre>
 *   - 한계: Interface에 Exception이 존재할 경우, Service 에서도 Checked Exception에 의존할 수 밖에 없다.
 * </pre>
 */
@Slf4j
public class MemberServiceV4_interfaceWithEx {


  private final MemberRepositoryEx memberRepository;


  public MemberServiceV4_interfaceWithEx(MemberRepositoryEx memberRepository) {
    this.memberRepository = memberRepository;


  }


  @Transactional
  public void accountTransfer(String fromId, String toId, int money) throws SQLException {
    bizLogic(fromId, toId, money);
  }

  private void bizLogic(String fromId, String toId, int money) throws SQLException {
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
