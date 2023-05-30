package core.spring.service;

import core.spring.domain.Member;
import core.spring.repository.MemberRepository;
import core.spring.repository.MemoryMemberRepository;

public class MemberServiceImpl implements MemberService{

  private final MemberRepository memberRepository = new MemoryMemberRepository();



  @Override
  public void join(Member member) {
    memberRepository.save(member);
  }

  @Override
  public Member findMember(Long memberId) {
    return memberRepository.findById(memberId);
  }

}
