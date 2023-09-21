package hello.itemservice.config;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jpa.JpaItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Configuration
public class JpaConfig {

  private final EntityManager em; // 빨간불없다고 에러뜨지만, 정상적으로 주입된다. by KYH 강사님.

  @Bean
  ItemService itemService() {
    return new ItemServiceV1(itemRepository());
  }

  @Bean
  ItemRepository itemRepository() {
    return new JpaItemRepository(em);
  }

}
