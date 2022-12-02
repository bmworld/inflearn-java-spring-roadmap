package hellospring.service;

import hellospring.domain.Member;
import hellospring.repository.MemberRepository;
import hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;



// service 코드는 해당 비지니스에 맞게, 해당 용어들( find, join)을 포함한 Business Logic을 짠다.
// repository 코드는 개발자스럽게(?) 데이터 저장, 읽기, 수정, 삭제 등의 기능 중심으로 코드를 구성한다.

//@Service
//@Transactional // <-- 회원가입할 때만, JPA를 쓸거라서, 클래스 자체에 Anotation걸어두지 않아도 된다.
// - @Transactional: 데이터를 저장하고 변경할 때 항상 필요
// - @Transactional: Spring with JPA를 사용하려면, 반드시 붙여줘야한다. (클래스에 어노테이션 또는.... 메서드위에 어노테이션으로 붙이기)
public class MemberService {

  // !!!! new Instance Ver.
  // ===> 리포지토리가 하나가 아니라, 여러 개가 생길 위험성있음. (static keyword가 없었다면 확실히 그랬을거다)
  //  private final MemberRepository memberRepository = new MemoryMemberRepository();
  // 해당 리포지토리를 contructor 생성자를 통해 주입하는 방식으로 하여, instance가 생성되더라도, 불변하도록 설정해준다.

  // !!! Initialize Ver.
//  @Autowired
  private final MemberRepository memberRepository;
  public MemberService (MemberRepository memberRepository) {
    this.memberRepository = memberRepository;

  }


  /*
  * 회원 가입
  * */
  // ! Spring with JPA를 사용하려면, 반드시 붙여줘야한다. (클래스에 어노테이션 또는.... 메서드위에 어노테이션으로 붙이기)
  // !  JPA는 모든 데이터변경이 실행될 때, Transactional 내부에서 실행되어야 한다.
  @Transactional
  public Long join(Member member) {
    long start = System.currentTimeMillis();
    try {
      validateDuplicateMember(member); // condition : 동명이인 회원가입 불가.
      memberRepository.save(member);
      return member.getId();
    } finally {
      long finish = System.currentTimeMillis();
      long timeMs = finish - start;
      System.out.println("메서드 시간측정 > join = " + timeMs + "ms");
    }



  }

  private void validateDuplicateMember(Member member) {
    memberRepository.findByName(member.getName()).ifPresent(m -> {
      throw new IllegalStateException("이미 존재하는 회원입니다.");
    });
  }

  public List<Member> findMembers(){
    long start = System.currentTimeMillis();
    try {
      return memberRepository.findAll();
    } finally {
      long finish = System.currentTimeMillis();
      long timeMs = finish - start;
      System.out.println("메서드 시간측정 > findMembers = " + timeMs + "ms");
    }
  }

  public Optional<Member> findOne(Long memberId){
    return memberRepository.findById(memberId);

  }





}
