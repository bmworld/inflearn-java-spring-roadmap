package hello.login.domain.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@AllArgsConstructor
public class Member {
  private Long id;

  @NotEmpty
  private String loginId;
  @NotEmpty
  private String name;
  @NotEmpty
  private String password;



}
