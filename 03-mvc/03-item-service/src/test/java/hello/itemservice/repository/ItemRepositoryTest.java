package hello.itemservice.repository;

import hello.itemservice.domain.Item;
import hello.itemservice.dto.UpdateItemRequestDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRepositoryTest {

  ItemRepository itemRepository = new ItemRepository();

  @AfterEach
  void afterEach() {
    itemRepository.clearStore();
  }


  @Test
  @DisplayName("Test Save")
  public void save() throws Exception {
    // Given / When
    Item savedItem = saveItem();


    // Then
    Item foundItem = itemRepository.findById(savedItem.getId());
    assertThat(savedItem).isEqualTo(foundItem);

  }

  @Test
  @DisplayName("Test FindAll")
  public void findAll() throws Exception {
    // Given

    int allItemCount = 5;
    Map<Long, Item> savedItems = new HashMap<>();
    for (int i = 0; i < allItemCount; i++) {
      Integer curOrder = i + 1;
      Item item = new Item("item-" + curOrder, 1000 * curOrder, 1 * curOrder);
      itemRepository.save(item);
      savedItems.put(item.getId(), item);
    }

    // When
    List<Item> foundItems = itemRepository.findAll();

    // Then
    assertThat(foundItems.size()).isEqualTo(allItemCount);
    for (Item foundItem : foundItems) {
      Long curFoundItemId = foundItem.getId();
      Item savedItem = savedItems.get(curFoundItemId);
      assertThat(foundItem).isEqualTo(savedItem);
    }

  }


   @Test
   @DisplayName("Test updateItem")
   public void updateItem() throws Exception {
     // Given / When
     Item savedItem = saveItem();
     Long targetItemId = savedItem.getId();

     // When
     UpdateItemRequestDto requestDto = UpdateItemRequestDto.builder()
       .itemName("updatedItem")
       .price(20000)
       .quantity(20)
       .build();

     itemRepository.update(targetItemId, requestDto);
     // Then
     Item foundItem = itemRepository.findById(targetItemId);
     assertThat(foundItem).isEqualTo(savedItem);
     assertThat(foundItem.getItemName()).isEqualTo(requestDto.getItemName());
     assertThat(foundItem.getPrice()).isEqualTo(requestDto.getPrice());
     assertThat(foundItem.getQuantity()).isEqualTo(requestDto.getQuantity());


   }

  private Item saveItem() {
    Item item = new Item("itemA", 10000, 10);
    return itemRepository.save(item);
  }
}
