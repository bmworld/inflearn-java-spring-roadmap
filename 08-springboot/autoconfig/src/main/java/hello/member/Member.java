package hello.member;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Data
public class Member {
  private String memberId;
  private String name;

  public Member(String memberId, String name) {
    this.memberId = memberId;
    this.name = name;
  }
}
