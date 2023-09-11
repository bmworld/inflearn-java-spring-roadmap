package hello.itemservice.repository.jdbcTemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * JDBC Template
 */
@Slf4j
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

  private final NamedParameterJdbcTemplate template;

  public JdbcTemplateItemRepositoryV2(DataSource dataSource) {
    template = new NamedParameterJdbcTemplate(dataSource);
  }

  @Override
  public Item save(Item item) {
    String sql = "insert into item(item_name, price, quantity) " +
      "values (:itemName, :price, :quantity)";

    BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(item);

    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(sql, param, keyHolder);


    long key = keyHolder.getKey().longValue();
    item.setId(key);
    return item;
  }

  @Override
  public void update(Long itemId, ItemUpdateDto updateParam) {
    String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";
    MapSqlParameterSource param = new MapSqlParameterSource()
      .addValue("itemName", updateParam.getItemName()).addValue("price", updateParam.getPrice()).addValue("quantity", updateParam.getQuantity())
      .addValue("id", itemId);

    template.update(sql, param);

  }

  @Override
  public Optional<Item> findById(Long id) {
    String sql = "select id, item_name, price, quantity from item where id=:id";
    try {
      Map<String, Long> param = Map.of("id", id);
      Item item = template.queryForObject(sql, param, itemRowMapper());
      return Optional.of(item);
    } catch (EmptyResultDataAccessException e) {
      // ! queryForObject: 결과 없을 시, EmptyResultDataAccessException 발생.
      //  => try catch 절에서 결과가 없을 경우 대응
      return Optional.empty();
    }


  }

  /**
   * <h1>RowMapper: `DB Row -> Item` </h1>
   * {@link RowMapper}: DB 반환 결과인 ResultSet을 객체로 변환
   */
  private RowMapper<Item> itemRowMapper() {
    return BeanPropertyRowMapper.newInstance(Item.class); //Item Fields 값을 자동으로 매핑시켜줌 & Camel Case 지원
  }

  @Override
  public List<Item> findAll(ItemSearchCond cond) {
    String sql = "select id, item_name, price, quantity from item";

    String itemName = cond.getItemName();
    Integer maxPrice = cond.getMaxPrice();

    BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(cond);


    // TODO: 동적쿼리
    if (StringUtils.hasText(itemName) || maxPrice != null) {
      sql += " where";
    }

    boolean andFlag = false;
    if (StringUtils.hasText(itemName)) {
      sql += " item_name like concat('%',:itemName,'%')";
      andFlag = true;
    }

    if (maxPrice != null) {
      if (andFlag) {
        sql += " and";
      }
      sql += " price <= :maxPrice";
//      andFlag = true;
    }

    log.info("sql={}", sql);

    return template.query(sql, param, itemRowMapper());
  }

  @Override
  public void deleteAll() {
    String sql = "delete from item";
    MapSqlParameterSource param = new MapSqlParameterSource();
    template.update(sql, param);
  }
}
