package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

/**
 * 참조
 * <pre>
 *   * @Transactional: JPA 모든 데이터 변경은 Transaction 내에서 이뤄져야함.
 *     - 조회는 Transaction 없이도 가능
 *     - 변경의 경우, 일반적으로 Service 계층에서 Transaction을 시작하기 때문에 문제가 없다.
 *     - 이번 예제는 복잡한 Service Logic이 없어서, Repository Layer에서 Transaction을 시작한다.
 *       -> 일반적으로는 Business Layer가 존재하는 Service Layer 에서 Transaction을 걸어준다.
 *   * SpringBoot는, JPA 설정에 필요한 EntityManagerFactory, JpaTransactionManager, DataSource 등을 모두 자동화한다.
 * </pre>
 */
@Repository
@Slf4j
@Transactional
@RequiredArgsConstructor
public class JpaItemRepository implements ItemRepository {

  private final EntityManager em; // Spring 통합 사용 시, Spring에 의해 DI 적용됨!

  @Override
  public Item save(Item item) {
    em.persist(item);
    return item;
  }

  @Override
  public void update(Long itemId, ItemUpdateDto updateParam) {
    Item foundItem = em.find(Item.class, itemId);
    foundItem.setItemName(updateParam.getItemName());
    foundItem.setPrice(updateParam.getPrice());
    foundItem.setQuantity(updateParam.getQuantity());

  }

  @Override
  public Optional<Item> findById(Long id) {
    Item item = em.find(Item.class, id);
    return Optional.ofNullable(item);
  }

  @Override
  public List<Item> findAll(ItemSearchCond cond) {
    String jpql = "SELECT i from Item i ";

    Integer maxPrice = cond.getMaxPrice();
    String itemName = cond.getItemName();

    // 조건문 시작
    if (hasText(itemName) || maxPrice != null) {
      jpql += " WHERE";
    }

    // PARAM - itemName
    boolean andFlag = false;
    List<Object> param = new ArrayList<>();
    if (hasText(itemName)) {
      jpql += " i.itemName LIKE CONCAT('%', :itemName, '%')";
      param.add(itemName);
      andFlag = true;
    }

    // PARAM - maxPrice
    if (maxPrice != null) {
      if (andFlag) {
        jpql += " AND";
      }

      jpql += " i.price  <= :maxPrice";
      param.add(maxPrice);
    }

    TypedQuery<Item> query = em.createQuery(jpql, Item.class);

    if (hasText(itemName)) {
      query.setParameter("itemName", itemName);
    }

    if (maxPrice != null) {
      query.setParameter("maxPrice", maxPrice);
    }

    log.info("jpql = {}", jpql);

    return query.getResultList();
  }


  @Override
  public void deleteAll() {
    String jpql = "DELETE FROM Item";
    em.createQuery(jpql)
      .executeUpdate();
  }
}
