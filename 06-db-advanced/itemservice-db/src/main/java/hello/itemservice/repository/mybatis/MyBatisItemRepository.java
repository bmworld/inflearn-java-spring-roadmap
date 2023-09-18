package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <h1>MyBatis 설정 원리</h1>
 * <pre>
 *   * MyBatis Spring 연동 모듈 작동 로직
 *     1) @Mapper Annotation 조회 -> <<interface>>ItemMapper
 *     2) 동적 Proxy 객체 생성 (<<proxy>> ItemMapper 구현체
 *     3) Proxy 객체를 Spring Bean 등록
 *
 *   * 정리
 *     - Mapper 구현체 (proxy 객체) 덕분에 MyBatis를 Spring에 쉽게 통합하여 사용할 수 잇음
 *     - Mapper 구현체 사용 시, Spring Exception 추상화도 함께 적용됨
 *     -  MyBatis Spring 연동 모듈은 많은 부분을 자동으로 설정해줌 ex. DB Connection, Transaction 등 MyBatis와 자동 연동 및 동기화
 * </pre>
 */
@Slf4j
@Repository
@RequiredArgsConstructor
public class MyBatisItemRepository implements ItemRepository {

  private final ItemMapper itemMapper; // @Mapper 어노테이션이 있을 경우, 스프링이 알아서 구현체 만들어서, Spring Bean으로 주입까지 해준다. (by Proxy)
  @Override
  public Item save(Item item) {
    log.info("[MyBatis Proxy 객체 = interface 구현체 확인하기] itemMapper class={}", itemMapper.getClass()); // 결과: class=class jdk.proxy2.$Proxy67
    itemMapper.save(item);
    return item;
  }

  @Override
  public void update(Long itemId, ItemUpdateDto updateParam) {
    itemMapper.update(itemId, updateParam);
  }

  @Override
  public Optional<Item> findById(Long id) {
    return itemMapper.findById(id);
  }

  @Override
  public List<Item> findAll(ItemSearchCond cond) {
    return itemMapper.findAll(cond);
  }

  @Override
  public void deleteAll() {
    itemMapper.deleteAll();
  }
}
