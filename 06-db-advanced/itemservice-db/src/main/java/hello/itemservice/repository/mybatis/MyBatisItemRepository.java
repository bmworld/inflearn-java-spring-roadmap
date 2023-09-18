package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class MyBatisItemRepository implements ItemRepository {

  private final ItemMapper itemMapper; // @Mapper 어노테이션이 있을 경우, 스프링이 알아서 구현체 만들어서, Spring Bean으로 주입까지 해준다. (by Proxy)
  @Override
  public Item save(Item item) {
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

  }
}
