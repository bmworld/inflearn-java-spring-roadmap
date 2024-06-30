package memory;

import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Slf4j
public class MemoryCondition implements Condition {

  public static final String ON = "on";

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    // -DMemory-on
    String memory = context.getEnvironment().getProperty("memory");

    boolean matches = Objects.equals(memory, ON);
    log.info("--- Memory = {} / matches = {}", memory, matches);
    return matches;
  }
}
