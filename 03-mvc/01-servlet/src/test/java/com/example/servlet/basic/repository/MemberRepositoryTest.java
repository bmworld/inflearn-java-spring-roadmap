package com.example.servlet.basic.repository;

import com.example.servlet.domain.member.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest {

  MemberRepository memberRepository = MemberRepository.getInstance();

  @AfterEach
  void afterEach() {
    memberRepository.clearStore();

    // 매 TEST 종료 시, store 초기화 한다.
  }

  @DisplayName("test findById() Method")
  @Test
  void testSave () {
    // Given
    Member member = new Member("hello", 20);
    // When

    Member savedMember = memberRepository.save(member);

    // Then
    Member foundMember = memberRepository.findById(savedMember.getId());
    assertThat(foundMember).isEqualTo(member);
    assertThat(foundMember).isEqualTo(savedMember);
    assertThat(member).isEqualTo(savedMember);


  }


  @DisplayName("test findAll() Method")
  @Test
  void testFindAll () {
    // Given
    int allSize = 10;
    List<Member> members = new ArrayList<>();
    for (int i = 0; i < allSize; i++) {
      String username = "hello-" + i;
      int age = 20 + i;
      Member member = new Member(username, age);
      Member savedMember = memberRepository.save(member);
      members.add(savedMember);
    }
    // When
    List<Member> foundMembers = memberRepository.findAll();

    // Then
    assertThat(foundMembers.size()).isEqualTo(members.size());


    int lastIndex = allSize - 1;
    for (int i = 0; i < allSize; i++) {
      Member foundMember = foundMembers.get(i);
      Member savedMember = members.get(i);
      assertThat(foundMember).isEqualTo(savedMember);
      assertThat(foundMembers).contains(savedMember);
    }

  }


}
