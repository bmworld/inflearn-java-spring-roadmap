package hello.thymeleafbasic.basic.dto;


import lombok.Data;

@Data
public class User {
  private String username;
  private int age;

  public User(String name, int age) {
    this.username = name;
    this.age = age;
  }
}
