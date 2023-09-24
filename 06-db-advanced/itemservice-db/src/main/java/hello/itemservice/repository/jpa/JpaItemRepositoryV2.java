package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

/**
 * 참조) @Repository 없어도, 예외처리 다 된다. (나의 추정: Spring Data JPA 내에서 Handling 하는 것으로보임.)
 */
@Slf4j
@RequiredArgsConstructor
@Repository
@Transactional
public class JpaItemRepositoryV2 implements ItemRepository {

  private final SpringDataJpaItemRepository itemRepository;
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
  public List<Item> findAll(ItemSearchCond cond) {

    Integer maxPrice = cond.getMaxPrice();
    String itemName = cond.getItemName();

    // 조건문 시작
    if (hasText(itemName) && maxPrice != null) {
      // * Spring Data Jpa 이름사용 Ver.
//      return itemRepository.findByItemNameLikeAndPriceLessThanEqual(itemName, maxPrice); // Spring Data jpa의 이름만으로 만들 수도 있음

      // * Spring Data JPA @Query Ver.
      return itemRepository.findItemsByQueryAnnotation(likeItemName(itemName), maxPrice);
    } else if (hasText(itemName)) {
      return itemRepository.findByItemNameLike(likeItemName(itemName));
    } else if (maxPrice != null) {
      return itemRepository.findByPriceLessThanEqual(maxPrice);
    } else {
      return itemRepository.findAll();
    }
  }

  private String likeItemName(String itemName) {
    return "%" + itemName + "%"; // ! hibernate 5.6.7 ver. 버그가 있어서, 버전변경 -> "5.6.5.Final"
  }


  @Override
  public void deleteAll() {

    itemRepository.deleteAll();
  }
}
