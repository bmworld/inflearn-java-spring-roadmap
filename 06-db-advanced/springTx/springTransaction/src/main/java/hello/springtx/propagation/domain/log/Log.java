package hello.springtx.propagation.domain.log;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * DB에 남기는 Log Entity
 */
@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Log {
  @Id
  @GeneratedValue
  private Long id;

  private String message;

  public Log(String message) {
    this.message = message;
  }
}
