package core.spring.service;

import core.spring.domain.Member;
import core.spring.repository.MemberRepository;
import core.spring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("CustomBeanName_MemberServiceImpl")
public class MemberServiceImpl implements MemberService{

  private final MemberRepository memberRepository;

  //  Spring은 `생성자`에 Autowired가 붙어있는 경우, 자동으로 그에 맞는 Class를 주입시켜줌
  @Autowired  // like `ac.getBean(MemberRepository.class)`
  public MemberServiceImpl(MemoryMemberRepository memberRepo) {
    this.memberRepository = memberRepo;
  }


  @Override
  public void join(Member member) {
    memberRepository.save(member);
  }

  @Override
  public Member findMember(Long memberId) {
    return memberRepository.findById(memberId);
  }


  // TEST 용도
  public MemberRepository getMemberRepository() {
    return memberRepository;
  }
}
