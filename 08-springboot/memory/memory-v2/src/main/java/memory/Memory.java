package memory;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Memory {
  private long used;
  private long max;

  public Memory(long used, long max) {
    this.used = used;
    this.max = max;
  }
}
