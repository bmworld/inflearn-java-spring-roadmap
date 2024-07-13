package hello.external;

import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.DefaultApplicationArguments;

/**
 *
 *
 * <h1>Commandline 인수 사용하기 - 커맨드 라인 옵션 </h1>
 *
 * <pre>
 *   - Java 표준은 아니다.
 *   - Spring 에서 커맨드라인인수를 좀더 편리하게 사용할 수 있도록 하기 위해 추가함
 *   - e.g) `--username=bm`
 *     > java --username="bm -jar ...
 * </pre>
 */
@Slf4j
public class CommandLineV2 {
  public static void main(String[] args) {
    // --url=myurl --username=bm --password=helllo mode=on
    for (String arg : args) {
      log.info("arg: {}", arg);
      // IDE
    }

    // Spring 이 해당 옵션값을 parsing 한 값을 사용해보자.
    DefaultApplicationArguments appArgs = new DefaultApplicationArguments(args);
    // 모든 Args 확인
    log.info("SourceArgs = {}", List.of(appArgs.getSourceArgs()));
    // SourceArgs = [--url=myurl, --username=bm, --password=helllo, mode=on]

    // 하이픈 없는 Args 확인
    log.info("NonOptionArgs = {}", appArgs.getNonOptionArgs());
    // NonOptionArgs = [mode=on]

    // - 하이픈 2개 사용한 args의 key 가져옴
    Set<String> optionNames = appArgs.getOptionNames();
    for (String optionName : optionNames) {
      log.info("option: key={}, value={}", optionName, appArgs.getOptionValues(optionName));
      // option: key=password, value=[hello]

    }
  }
}
