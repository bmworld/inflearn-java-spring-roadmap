package hello.itemservice.repository.jdbcTemplate;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC Template
 */
@Slf4j
public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

  private final JdbcTemplate template;

  public JdbcTemplateItemRepositoryV1(DataSource dataSource) {
    template = new JdbcTemplate(dataSource);
  }

  @Override
  public Item save(Item item) {
    String sql = "insert into item(item_name, price, quantity) values (?,?,?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    template.update(con -> {
      // 자동증가 키,
      PreparedStatement ps = con.prepareStatement((sql), new String[]{"id"});
      ps.setString(1, item.getItemName());
      ps.setInt(2, item.getPrice());
      ps.setInt(3, item.getQuantity());
      return ps;
    }, keyHolder);

    long key = keyHolder.getKey().longValue();
    item.setId(key);
    return item;
  }

  @Override
  public void update(Long itemId, ItemUpdateDto updateParam) {
    String sql = "update item set item_name=?, price=?, quantity=? where id=?";
    template.update(sql,
      updateParam.getItemName(),
      updateParam.getPrice(),
      updateParam.getQuantity(),
      itemId);
  }

  @Override
  public Optional<Item> findById(Long id) {
    String sql = "select id, item_name, price, quantity from item where id=?";
    try {
      Item item = template.queryForObject(sql, itemRowMapper(), id);
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
    return (rs, rowNum) -> {
      Item item = new Item();
      item.setId(rs.getLong("id"));
      item.setItemName(rs.getString("item_name"));
      item.setPrice(rs.getInt("price"));
      item.setQuantity(rs.getInt("quantity"));
      return item;
    };
  }

  @Override
  public List<Item> findAll(ItemSearchCond cond) {
    String sql = "select id, item_name, price, quantity from item";

    String itemName = cond.getItemName();
    Integer maxPrice = cond.getMaxPrice();

    // TODO: 동적쿼리
    if (StringUtils.hasText(itemName) || maxPrice != null) {
      sql += " where";
    }

    boolean andFlag = false;
    List<Object> param = new ArrayList<>();
    if (StringUtils.hasText(itemName)) {
      sql += " item_name like concat('%',?,'%')";
      param.add(itemName);
      andFlag = true;
    }

    if (maxPrice != null) {
      if (andFlag) {
        sql += " and";
      }
      sql += " price <= ?";
      param.add(maxPrice);
//      andFlag = true;
    }

    log.info("sql={}", sql);

    return template.query(sql, itemRowMapper(), param.toArray());
  }

  @Override
  public void deleteAll() {
    String sql = "delete from item";
    template.update(sql);
  }
}
