package hello;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

/**
 *
 *
 * <h1>Commandline option 인수와 Springboot</h1>
 *
 * <pre>
 *   - IDE > Configuration > Program Arguments 에 특정 값을 넣어주면, 해당 값을 읽어들인다.
 *   - Bean 등록이 된 것이니깐, ExternalApplication 실행을 통하여, 아래 값을 확인하시라.
 * </pre>
 */
@Slf4j
@Component
public class CommandLineBean {
  private final ApplicationArguments arguments; // Spring 이 알아서 주입해준다.

  public CommandLineBean(ApplicationArguments arguments) {
    this.arguments = arguments;
  }

  @PostConstruct
  public void init() {
    System.out.println("CommandLineBean.init");

    log.info("Source = {}", List.of(arguments.getSourceArgs()));
    Set<String> optionNames = arguments.getOptionNames();
    log.info("OptionNames = {}", optionNames);
    for (String optionName : optionNames) {
      log.info("--- optionName={} value={}", optionName, arguments.getOptionValues(optionName));
    }
  }
}
