package hello.itemservice.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static hello.itemservice.domain.QItem.*;
import static org.springframework.util.StringUtils.hasText;

/**
 * 참조) @Repository 없어도, 예외처리 다 된다. (나의 추정: Spring Data JPA 내에서 Handling 하는 것으로보임.)
 */
@Slf4j
@Repository
@Transactional
public class JpaItemRepositoryV3 implements ItemRepository {

  private final EntityManager em;
  private final JPAQueryFactory queryFactory;

  public JpaItemRepositoryV3(EntityManager em) {
    this.em = em;
    this.queryFactory = new JPAQueryFactory(em);
  }



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

  public List<Item> findAll_old(ItemSearchCond cond) {

    Integer maxPrice = cond.getMaxPrice();
    String itemName = cond.getItemName();

    BooleanBuilder builder = getBooleanBuilderByItemNameAndMaxPrice(itemName, maxPrice);

    return queryFactory.selectFrom(item)
      .where(
        builder
      )
      .fetch();
  }


  @Override
  public List<Item> findAll(ItemSearchCond cond) {
    return queryFactory.selectFrom(item)
      .where(
        containsItemName(cond.getItemName()),
        loeMaxPrice(cond.getMaxPrice())
      )
      .fetch();
  }
  private BooleanBuilder getBooleanBuilderByItemNameAndMaxPrice(String itemName, Integer maxPrice) {
    BooleanBuilder builder = new BooleanBuilder();
    if (hasText(itemName)) {
      builder.and(item.itemName.like("%" + itemName + "%"));
    }

    if (maxPrice != null) {
      builder.and(item.price.loe(maxPrice));
    }
    return builder;
  }


  @Override
  public void deleteAll() {
    em.createQuery("DELETE FROM Item")
      .executeUpdate();
  }

  private BooleanExpression containsItemName(String itemName) {
    if(!hasText(itemName)) return null;
    return item.itemName.contains(itemName);
  }

  private BooleanExpression loeMaxPrice(Integer maxPrice) {
    if(maxPrice == null) return null;
    return item.price.loe(maxPrice);
  }
}
