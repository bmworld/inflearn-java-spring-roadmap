package hellospring.domain;

import javax.persistence.*;

@Entity
public class Member {

  @Id
  @GeneratedValue (strategy = GenerationType.IDENTITY) // ! @GeneratedValue => DB 가 알아서 IDENTITY를 생성하도록 만든다.
  private Long id; // 데이터를 구분하기 위해서 시스템에 저장하는 id임 // 고객을 구분하기 위한 id가 아님.

//  @Column(name = "NAME") // - EXAMPLE => JPA > Anotation > Column => DB의 "NAME" Column에 자동 매핑해준다.
  private String name;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
