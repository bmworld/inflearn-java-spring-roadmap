package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpringDataJpaItemRepository extends JpaRepository<Item, Long> {

  List<Item> findByItemNameLike(String itemName);
  List<Item> findByPriceLessThanEqual(Integer price);

  // Query Method
  List<Item> findByItemNameLikeAndPriceLessThanEqual(String itemName, Integer price);


  // Query 직접 실행하는 경우
  @Query("SELECT i FROM  Item i WHERE i.itemName like :itemName AND i.price <= :price")
  List<Item> findItemsByQueryAnnotation(@Param(("itemName")) String itemName, @Param("price") Integer price);

}
