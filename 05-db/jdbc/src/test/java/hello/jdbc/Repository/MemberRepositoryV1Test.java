package hello.jdbc.Repository;

import com.zaxxer.hikari.HikariDataSource;
import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.UUID;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
class MemberRepositoryV1Test {

  private MemberRepositoryV1 repository;

  /**
   * <h1>Driver Manager Ver.</h1>
   */
//  @BeforeEach
//  private void init() throws SQLException {
//    // 기본 Driver Manager: 항상 새로운 Connection 획득
//    DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
//    repository = new MemberRepositoryV1(dataSource);
//  }

  /**
   * <h1>Connection Pool Ver.</h1>
   * <pre>
   *   Connection close 사용 시, 동일한 Connection 재사용 가능
   *   (Connection 닫기 X / Connection Pool에 반환)
   *
   *
   *   ex)
   *   getConnection=HikariProxyConnection@878668275 wrapping conn13: url=jdbc:h2:tcp://localhost/~/h2/spring-db user=SA, class=class com.zaxxer.hikari.pool.HikariProxyConnection
   *   getConnection=HikariProxyConnection@1356732524 wrapping conn13: url=jdbc:h2:tcp://localhost/~/h2/spring-db user=SA, class=class com.zaxxer.hikari.pool.HikariProxyConnection
   * </pre>
   */
  @BeforeEach
  private void init() throws SQLException {
    // 기본 Driver Manager: 항상 새로운 Connection 획득
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setJdbcUrl(URL);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setMaximumPoolSize(10);
    dataSource.setPoolName("MyPool");
    repository = new MemberRepositoryV1(dataSource);
  }

  @Test
  void save() throws SQLException {
    String memberId = createMemberId();
    Member member = generateMember(memberId, 10000);

    assertThat(member.getMemberId()).isEqualTo(memberId);
  }

  @Test
  @DisplayName("findMemberById")
  public void findMemberById() throws Exception {

    // Given
    Member member = generateMember();
    // when
    Member foundMember = repository.findById(member.getMemberId());
    log.info("foundMember={}", foundMember);
    log.info("member == foundMember => {}", (member == foundMember)); // false => instances 다름
    log.info("member.equals(foundMember) => {}", (member.equals(foundMember))); // true => equalsAndHashCode 비교는 동일

    // Then
    assertThat(foundMember).isEqualTo(member);


  }


  @Test
  @DisplayName("update")
  public void update() throws Exception {
    // Given
    int beforeMoney = 10000;
    int afterMoney = 20000;
    Member member = generateMember(beforeMoney);
    // When
    repository.update(member.getMemberId(), afterMoney);
    // Then

    Member foundMember = repository.findById(member.getMemberId());
    assertThat(foundMember.getMoney()).isEqualTo(afterMoney);

  }


  @Test
  @DisplayName("delete")
  public void delete() throws Exception {
    // Given
    Member member = generateMember();
    // When
    repository.delete(member.getMemberId());
    // Then
    assertThatThrownBy(() ->
      repository.findById(member.getMemberId())).isInstanceOf(NoSuchElementException.class);

  }

  private Member generateMember(String memberId, int money) throws SQLException {
    Member member = new Member(memberId, money);
    repository.save(member);
    return member;
  }

  private Member generateMember(int money) throws SQLException {
    return generateMember(createMemberId(), money);
  }

  private Member generateMember() throws SQLException {
    return generateMember(10000);
  }


  private static String createMemberId() {
    String uuid = UUID.randomUUID().toString();
    return "member-" + uuid.substring(0, 3);
  }
}
