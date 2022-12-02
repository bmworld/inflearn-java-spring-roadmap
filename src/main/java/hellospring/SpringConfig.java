package hellospring;


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
  ///////// * JdbcRepository VER. ///////////
  private EntityManager em;

  public SpringConfig(EntityManager em) {
    this.em = em;
  }
  ///////////////////////////////////////////////////////////////////////

  @Bean
  public MemberService memberService() {
    return new MemberService(memberRepository());
  }


  @Bean
  public MemberRepository memberRepository() {
//    return new MemoryMemberRepository(); // MemoryDB Ver. (스피링 서버 내리면, DB내용 휘발됨)
//    return new JdbcMemberRepository(dataSource); // OG JDBC Ver.
//    return new JdbcTemplateMemberRepository(dataSource); // JDBC Template Ver.
    return new JpaMemberRepository(em); // JPA Ver.
  }


}
