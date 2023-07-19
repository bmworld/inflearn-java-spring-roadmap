package hello.itemservice.repository;

import hello.itemservice.domain.Item;
import hello.itemservice.dto.UpdateItemRequestDto;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {
  private static final Map<Long, Item> store = new HashMap<>();

  private static long sequence = 0L; //static


  public Item save(Item item) {
    item.setId(++sequence);
    store.put(item.getId(), item);
    return item;
  }

  public Item findById(Long id) {
    return store.get(id);
  }

  public List<Item> findAll() {
    return new ArrayList<>(store.values()); // ArrayList 로 감써서 반환하면, store 값에 대한 변경 점 없이, 안정적으로 데이터를 전달할 수 있다."
  }

  public Long update(Long itemId, UpdateItemRequestDto dto) {
    Item foundItem = findById(itemId);
    foundItem.updateItem(dto);

    return foundItem.getId();
  }


  /**
   * ONLY FOR `TEST CODE`!!!
   */
  public void clearStore() {
    store.clear(); // Hash Data 초기화
  }
}
