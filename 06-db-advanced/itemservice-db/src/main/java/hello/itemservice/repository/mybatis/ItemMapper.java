package hello.itemservice.repository.mybatis;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Optional;

/**
 * <h1>MyBatis - Mapper </h1>
 * <pre>
 *   - 해당 interface에 '@Mapper' Annotation 추가해줘야, MyBatis 에서 인식가능.
 *   - interface의 Method 호출 시, `xml`의 해당 SQL을 실행하고 결과를 반환한다.
 *   - 해당 interface 구현체는 자동으로 만들어진다.
 *   - xml 파일 경로는 해당 interface경로의 루트를 `resource` 경로로 치환한 것과 동일하게 만든다.
 *   ex. main > java > hello.itemservice.repository > ItemMapper interface
 *       => resource > hello.itemservice.repository > ItemMapper.xml
 * </pre>
 *
 * <h1>MyBatis - XML 파일 작성 방법</h1>
 * <pre>
 *   - insert SQL: `<insert>`사용
 *   - id property에는 interface에 설정한 메서드명 기입
 *   - parameter는 `#{}` 문법 사용 -> Mapper 에서 전달한 객체의 Property 이름을 적는다.
 *   - `useGeneratedKeys`: DB가 Key를 생성해주는 `IDENTITY` 전략일 경우 사용.
 *   - `keyProperty`: 생성되는 Key의 속성명을 적는다.
 *      ex) DB insert 종료 후, `Item` 객체의 `id` 속성에 DB가 생성한 값이 자동할당된다.
 *   - Parameter: 2개 이상 시, `@Param`으로 이름을 지정하여 Parameter 구분 (* 1개: 불필요)
 *   - `resultType`: 반환할 타입 명시
 *     (해당 객체의 전체 package 경로 / 또는 application.properties에 명시할 경우, 축약 가능)
 *     ex. mybatis.type-aliases-package=hello.itemservice.domain
 *       전체기입 CASE => resultType="hello.itemservice.domain.Item"
 *       축약 CASE => resultType="Item"
 *   - `<if>...</if>`: 해당 test property 만족 시, 내부 구문 추가한다.
 *   - `<where></where>`: 적절하게 Where 문을 만들어준다.
 *     -> 만약 모든 `<if>`가 실패한 경우, SQL Where을 생성하지 않는다.
 *     -> 만약 하나 이상의 `<if>` 성공 시, 최초 `and`를 `where`로 변환해준다.
 *
 *   - XML 특수 문자
 *     : 홑화살표 등은 사용할 수 없으므로 Entity code를 사용한다. (ex. `>` =>  `&lt;`
 * </pre>
 */
@Mapper
public interface ItemMapper {

  void save(Item item);

  void update(@Param("id") Long id, @Param("updateParam") ItemUpdateDto updateParam);

  // Annotation 방식으로 사용가능 ([주의] xml파일과 중복 시, Exception)
  // Exception: o.m.spring.mapper.MapperFactoryBean      : Error while adding the mapper 'interface hello.itemservice.repository.mybatis.ItemMapper' to configuration.
//  @Select("SELECT id, item_name, price, quantity FROM ITEM WHERE id = #{id}")
  Optional<Item> findById(Long id);

  List<Item> findAll(ItemSearchCond itemSearchCond);

  void deleteAll();
}
