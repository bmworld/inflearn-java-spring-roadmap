package hello.itemservice.service;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import hello.itemservice.repository.v2.ItemQueryRepositoryV2;
import hello.itemservice.repository.v2.ItemRepositoryV2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceV2 implements ItemService {

  private final ItemRepositoryV2 itemRepository;
  private final ItemQueryRepositoryV2 itemQueryDslRepository;

  @Override
  public Item save(Item item) {
    return itemRepository.save(item);
  }

  @Override
  public void update(Long itemId, ItemUpdateDto updateParam) {
    Item foundItem = itemRepository.findById(itemId).orElseThrow();
    foundItem.setItemName(updateParam.getItemName());
    foundItem.setPrice(updateParam.getPrice());
    foundItem.setQuantity(updateParam.getQuantity());
  }

  @Override
  public Optional<Item> findById(Long id) {
    return itemRepository.findById(id);
  }

  @Override
  public List<Item> findItems(ItemSearchCond cond) {
    return itemQueryDslRepository.findAll(cond);
  }
}
