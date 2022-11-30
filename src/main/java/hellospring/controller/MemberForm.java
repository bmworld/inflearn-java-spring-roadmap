package hellospring.controller;

public class MemberForm {
  private String name; // input 태그의 name="name" <---이부분과 매칭이 되어서 값을 받을 수 있다.

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
