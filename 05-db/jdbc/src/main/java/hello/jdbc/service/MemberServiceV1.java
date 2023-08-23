package hello.jdbc.service;

import hello.jdbc.Repository.MemberRepositoryV1;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

/**
 * <h1>Transaction 미사용 Ver.</h1>
 * */
@RequiredArgsConstructor
public class MemberServiceV1 {

  private final MemberRepositoryV1 memberRepository;

  public void accountTransfer(String fromId, String toId, int money) throws SQLException {
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
