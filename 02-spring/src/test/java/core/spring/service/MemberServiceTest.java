package core.spring.service;

import core.spring.domain.Grade;
import core.spring.domain.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {
  private MemberService memberService = new MemberServiceImpl();
  @Test
  @DisplayName("join")
  public void join() throws Exception{
    // Given
    Long memberId = 1L;
    Member member = new Member(memberId, "hello", Grade.VIP);

    // When
    memberService.join(member);
    Member foundMember = memberService.findMember(memberId);

    // Then
    Assertions.assertThat(member).isEqualTo(foundMember);

  }

}
