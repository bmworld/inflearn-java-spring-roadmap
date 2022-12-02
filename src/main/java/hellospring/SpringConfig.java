package hellospring;
// * SpringConfig 파일 ->Spring Bean에 등록하는 파일이다.

import hellospring.aop.TimeTraceAop;
import hellospring.repository.*;
import hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

@Configuration
public class SpringConfig {


  ///////////////////////////////////////////////////////////////////////
  ///////// * MemoryRepository => JdbcRepository 로 변경을 위해 필요 ///////////
//  private final DataSource dataSource; // DataSource => Spring이 제공해준다!
//
//  @Autowired
//  public SpringConfig(DataSource dataSource) { // @Autowired를 통하여, 스프링으로부터 datasource를 주입받는다.
//    this.dataSource = dataSource;
//  }
//
  ///////// MemoryRepository => JdbcRepository 로 변경을 위해 필요 ///////////
  ///////////////////////////////////////////////////////////////////////


  ///////////////////////////////////////////////////////////////////////
  //////////////////// * JdbcRepository Ver.
//  private EntityManager em;
//
//  public SpringConfig(EntityManager em) {
//    this.em = em;
//  }
  ///////////////////////////////////////////////////////////////////////




  ///////////////////////////////////////////////////////////////////////
  //////////////////// * Spring Data JPA Ver.
  private final MemberRepository memberRepository;

  // - constructor에 injection을 하면, Spring JPA가 구현체를 만든 것이 등록된다.
  @Autowired // - Contructor 생성자가 하나인 경우에는 '생략'가능함.
  public SpringConfig(MemberRepository memberRepository) {
    this.memberRepository = memberRepository;
    // - Spring container 에서 등록한 것을 찾는다.
    // - 음. 뭘등록했니?
    // - 그거슨 바로....... SpringDataJpaMemberRepository라는 INTERFACE를 등록했따이거다.
    // - Interface만 만들어놓으면, Spring Data가 그 구현체를 지가 알아서 만든다 & Spring Bean에 등록한다.
  }
  ///////////////////////////////////////////////////////////////////////




  ///////////////////////////////////////////////////////////////////////
  @Bean
  public MemberService memberService() {
//    return new MemberService(memberRepository()); // * MemoryDB , JDBC, JDBC Template, JPA Ver.
    return new MemberService(memberRepository); // * Spring Data JPA Ver.
  }
///////////////////////////////////////////////////////////////////////



  ///////////////////////////////////////////////////////////////////////
  //////////////////// * MemoryDB , JDBC, JDBC Template, JPA Ver.
//  @Bean
//  public MemberRepository memberRepository() {
////    return new MemoryMemberRepository(); // MemoryDB Ver. (스피링 서버 내리면, DB내용 휘발됨)
////    return new JdbcMemberRepository(dataSource); // OG JDBC Ver.
////    return new JdbcTemplateMemberRepository(dataSource); // JDBC Template Ver.
//    return new JpaMemberRepository(em); // JPA Ver.
//  }
  ///////////////////////////////////////////////////////////////////////


//  @Bean // ! SpringBean에 직접 등록하는 방법  //=> 이거 대신, 해당 class 에  @Component Anotation붙이는 것으로도 가능함.
  public TimeTraceAop timeTraceAop () {
    return new TimeTraceAop();
  }
}
