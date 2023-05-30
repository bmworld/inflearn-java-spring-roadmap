package core.spring.service;

import core.spring.domain.Member;

public interface MemberService {
  void join(Member member);

  Member findMember(Long memberId);

}
