package hello.member;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberRepositoryTest {
  @Autowired private MemberRepository memberRepository;

  @Transactional
  @Test
  void memberTest() {
    Member member = new Member("idA", "memberA");
    memberRepository.initTable();
    memberRepository.save(member);

    Member foundMember = memberRepository.find(member.getMemberId());
    assertNotNull(foundMember);
    assertEquals(member.getMemberId(), foundMember.getMemberId());
    assertEquals(member.getName(), foundMember.getName());
  }
}
