package hellospring.service;

import hellospring.domain.Member;
import hellospring.repository.MemberRepository;
import hellospring.repository.MemoryMemberRepository;

import java.util.List;
import java.util.Optional;



// service 코드는 해당 비지니스에 맞게, 해당 용어들( find, join)을 포함한 Business Logic을 짠다.
// repository 코드는 개발자스럽게(?) 데이터 저장, 읽기, 수정, 삭제 등의 기능 중심으로 코드를 구성한다.

public class MemberService {

  // !!!! new Instance Ver.
  // ===> 리포지토리가 하나가 아니라, 여러 개가 생길 위험성있음. (static keyword가 없었다면 확실히 그랬을거다)
  //  private final MemberRepository memberRepository = new MemoryMemberRepository();
  // 해당 리포지토리를 contructor 생성자를 통해 주입하는 방식으로 하여, instance가 생성되더라도, 불변하도록 설정해준다.

  // !!! Initialize Ver.
  private final MemberRepository memberRepository;
  public MemberService (MemberRepository memberRepository) {
    this.memberRepository = memberRepository;

  }


  /*
  * 회원 가입
  * */
  public Long join(Member member) {
    // condition : 동명이인 회원가입 불가.
    validateDuplicateMember(member);
    memberRepository.save(member);
    return member.getId();
  }

  private void validateDuplicateMember(Member member) {
    memberRepository.findByName(member.getName()).ifPresent(m -> {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    });
  }

  public List<Member> findMembers(){
    return memberRepository.findAll();
  }

  public Optional<Member> findOne(Long memberId){
    return memberRepository.findById(memberId);

  }





}
