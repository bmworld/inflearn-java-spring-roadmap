package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jpa.JpaItemRepositoryV3;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Configuration
public class QueryDslConfig {

  private final EntityManager em;

  @Bean
  ItemService itemService() {
    return new ItemServiceV1(itemRepository());
  }

  @Bean
  ItemRepository itemRepository() {
    return new JpaItemRepositoryV3(em);
  }

}
