package com.example.servlet.basic.repository;


import com.example.servlet.domain.member.Member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 아래 예제는
 * 동시성 문제가 고려되어 있지 않음,
 * 실무에서는 ConcurrentHashMapp, AtomicLong 사용을 고려
 */
public class MemberRepository {
  private static Map<Long, Member> store = new HashMap<>();
  private static long sequence = 0L;

  private static final MemberRepository instance = new MemberRepository();


  // Singleton 으로 만들 경우, 기본 생성자를 만들 수 없게 함.
  private MemberRepository() {}

  public static MemberRepository getInstance() {
    return instance;
  }


  public Member save(Member member) {
    member.setId(++sequence);
    store.put(member.getId(), member);
    return member;
  }

  public Member findById(Long id) {
    return store.get(id);
  }

  public List<Member> findAll() {
    return new ArrayList<>(store.values()); // store 자체를 보호하기 위해서 new 생성자 사용
  }

  public void clearStore() {
    store.clear();
  }
}
