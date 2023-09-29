package hello.springtx.propagation;

import hello.springtx.propagation.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class MemberRepository{
  private final EntityManager em;

//  @Transactional(propagation = Propagation.REQUIRED) // cf) Propagation.REQUIRED => DEFAULT 값.
  public void save(Member member) {
    log.info("Save Member");
    em.persist(member);

  }

  public Optional<Member> findByUsername(String username) {
    return em.createQuery("select m from Member m where m.username = :username", Member.class)
      .setParameter("username", username)
      .getResultList()
      .stream().findAny() // 2개 이상 나올 경우, 1개만 반환.
      ;
  }
}
