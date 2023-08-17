package hello.jdbc.Repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class MemberRepositoryV0Test {

  private MemberRepositoryV0 repository = new MemberRepositoryV0();

  @BeforeEach
  private void init() throws SQLException {
    repository.deleteAll();
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
