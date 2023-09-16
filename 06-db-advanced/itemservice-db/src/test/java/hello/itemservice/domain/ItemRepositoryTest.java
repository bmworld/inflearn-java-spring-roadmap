package hello.itemservice.domain;

import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import hello.itemservice.repository.memory.MemoryItemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * <h1>TEST 주요 원칙</h1>
 * <pre>
 *   1. 각 테스트는 다른 테스트와 격리된 환경이다.
 *   2. 테스트는 반복해서 실행할 수 있다.
 * </pre>
 */

@Transactional // Spring: TEST transaction 안에서 실행 & 테스트 종료 시 Transaction 자동 rollback
@SpringBootTest // => 상위 패키지를 탐색하여 `@SpringBootApplication`을 찾는다. => 발견한 Application내의 설정들도 모두 사용한다. (ex. @Import(JdbcTemplateV3Config.class))
class ItemRepositoryTest {
//
//  @Autowired
//  private PlatformTransactionManager tm;
//  private TransactionStatus status;

  @Autowired
  private ItemRepository itemRepository;

//  @BeforeEach
//  void beforeEach() {
//    status = tm.getTransaction(new DefaultTransactionDefinition());
//  }

  @AfterEach
  void afterEach() {
    //MemoryItemRepository 의 경우 제한적으로 사용
    if (itemRepository instanceof MemoryItemRepository) {
      itemRepository.deleteAll();
    }


    // DB 격리화방법: transaction 시작 시, 초기 status 적용하여, rollback
//    tm.rollback(status);

  }


  @Test
  @Rollback(value = false)
  void save() {
    //given
    Item item = new Item("itemA", 10000, 10);

    //when
    Item savedItem = itemRepository.save(item);

    //then
    Item findItem = itemRepository.findById(item.getId()).get();
    assertThat(findItem).isEqualTo(savedItem);
  }

  @Test
  void updateItem() {
    //given
    Item item = new Item("item1", 10000, 10);
    Item savedItem = itemRepository.save(item);
    Long itemId = savedItem.getId();

    //when
    ItemUpdateDto updateParam = new ItemUpdateDto("item2", 20000, 30);
    itemRepository.update(itemId, updateParam);

    //then
    Item findItem = itemRepository.findById(itemId).get();
    assertThat(findItem.getItemName()).isEqualTo(updateParam.getItemName());
    assertThat(findItem.getPrice()).isEqualTo(updateParam.getPrice());
    assertThat(findItem.getQuantity()).isEqualTo(updateParam.getQuantity());
  }

  @Test
  void findItems() {
    itemRepository.deleteAll();
    //given
    Item item1 = new Item("itemA-1", 10000, 10);
    Item item2 = new Item("itemA-2", 20000, 20);
    Item item3 = new Item("itemB-1", 30000, 30);

    itemRepository.save(item1);
    itemRepository.save(item2);
    itemRepository.save(item3);

    //둘 다 없음 검증
    test(null, null, item1, item2, item3);
    test("", null, item1, item2, item3);

    //itemName 검증
    test("itemA", null, item1, item2);
    test("temA", null, item1, item2);
    test("itemB", null, item3);

    //maxPrice 검증
    test(null, 10000, item1);

    // 둘 다 있음 검증
    test("itemA", 10000, item1);
  }

  void test(String itemName, Integer maxPrice, Item... items) {
    List<Item> result = itemRepository.findAll(new ItemSearchCond(itemName, maxPrice));
    assertThat(result).containsExactly(items);
  }
}
