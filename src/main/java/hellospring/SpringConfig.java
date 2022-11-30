package hellospring;


import hellospring.repository.MemberRepository;
import hellospring.repository.MemoryMemberRepository;
import hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {

  @Bean
  public MemberService memberService () {
    return new MemberService(memberRepository());
  }

  @Bean
  public MemberRepository memberRepository () {
    return new MemoryMemberRepository();
  }


}
