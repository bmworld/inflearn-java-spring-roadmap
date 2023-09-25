package hello.itemservice.repository.v2;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static hello.itemservice.domain.QItem.item;
import static org.springframework.util.StringUtils.hasText;

@Repository
public class ItemQueryRepositoryV2 {
  private final JPAQueryFactory queryFactory;

  public ItemQueryRepositoryV2(EntityManager em) {
    this.queryFactory = new JPAQueryFactory(em);
  }

  public List<Item> findAll(ItemSearchCond cond) {
    return queryFactory.selectFrom(item)
      .where(
        containsItemName(cond.getItemName()),
        loeMaxPrice(cond.getMaxPrice())
      )
      .fetch();
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
