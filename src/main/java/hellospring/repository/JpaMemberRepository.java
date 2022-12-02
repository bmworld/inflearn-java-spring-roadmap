package hellospring.repository;

import hellospring.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository{

  private final EntityManager em;

  public JpaMemberRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Member save(Member member) {
    em.persist(member); // - 일캐하면....JPA가 알아서 INSERT QUERY를 짜서 DB에 데이터를 저장시켜준다.
    return member;
  }

  @Override
  public Optional<Member> findById(Long id) {
    Member member = em.find(Member.class, id);
    return Optional.ofNullable(member);
  }

  @Override
  public Optional<Member> findByName(String name) {
    // - ID와 다르게 JPQL이라는 쿼리를 사용해줘야한다.
    // select m from Member <--- 실제 객체명을 넣어준다. 대소문자 구분한다. (이걸로 에러를 경험했다ㅋㅋ)
    List<Member> result = em.createQuery("select m from Member m where m.name = :name", Member.class)
        .setParameter("name", name)
        .getResultList();
    return result.stream().findAny();
  }

  @Override
  public List<Member> findAll() {
    // * JTQL이다=> 객체를 향해서 Query를 날리면, 해당 데이터를 가져올 수 있다.
    // select m from Member : Member Entity를 조회해라고 쿼리를 날렸다.
    return em.createQuery("select m from Member", Member.class)
            .getResultList();
  }
}
