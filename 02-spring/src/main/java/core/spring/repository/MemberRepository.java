package core.spring.repository;

import core.spring.domain.Member;

public interface MemberRepository {
  void save(Member member);

  Member findById(Long memberId);
}
