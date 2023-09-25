package hello.itemservice;

import hello.itemservice.config.*;
import hello.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;


//@Import(MemoryConfig.class) // Memory Ver.
//@Import(JdbcTemplateV1Config.class)
//@Import(JdbcTemplateV2Config.class)
//@Import(JdbcTemplateV3Config.class)
//@Import(MyBatisConfig.class)
//@Import(JpaConfig.class)
//@Import(SpringDataJpaConfig.class)
//@Import(QueryDslConfig.class)
@Import(SpringDataJpaWithQueryDSLConfig.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")  // * 해당 Package만 Component Scan / 나머지는 수동 등록해야함
@Slf4j
public class ItemServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ItemServiceApplication.class, args);
  }

  @Bean
  @Profile("local")
  public TestDataInit testDataInit(ItemRepository itemRepository) {
    return new TestDataInit(itemRepository);
  }


  // TEST 전용 Embedded H2 DB 사용하기

  /**
   * 초기 데이터 넣는 방법
   * 해당 resources Directory 내에 `schema.sql` 이름을 가진 파일 생성 & SQL 넣으면 됨.
   * LOG Sample => o.s.jdbc.datasource.init.ScriptUtils     : Executing SQL script from URL [file://myDirectory.../test/resources/schema.sql]
   *
   * @return
   */
//  @Bean
//  @Profile("test")
//  public DataSource dataSource() {
//    log.info("--- Initializing memory Database");
//
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName("org.h2.Driver");
//    /**
//     * JVM 내에 Embedded mode H2 DB 사용
//     * DB_CLOSE_DELAY=-1 => Embdded Mode에서 DB connection이 모두 끊어질 경우 DB가 종료되는 것을 방지하는 설정
//     */
//    dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
//    dataSource.setUsername("sa");
//    dataSource.setPassword("");
//    return dataSource;
//  }

}
