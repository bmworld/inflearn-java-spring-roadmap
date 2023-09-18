package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jdbcTemplate.JdbcTemplateItemRepositoryV3;
import hello.itemservice.repository.mybatis.ItemMapper;
import hello.itemservice.repository.mybatis.MyBatisItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
public class MyBatisConfig {

  private final ItemMapper itemMapper; // 얘가 알아서 datasource 찾아서 주입받는다.

  @Bean
  ItemService itemService() {
    return new ItemServiceV1(itemRepository());
  }

  @Bean
  ItemRepository itemRepository() {
    return new MyBatisItemRepository(itemMapper);
  }

}
