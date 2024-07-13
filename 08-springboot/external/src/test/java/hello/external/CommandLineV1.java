package hello.external;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * <h1>Commandline 인수 사용하기</h1>
 *
 * <pre>
 *   1) IDE
 *   > CommandLineV1 에 대한 Configuration 설정 (Edit Configuration)
 *   > add VM Options
 *   > 환경변수 입력하면 됨. 구분자는 띄어쓰기
 *     ㄴ e.g.) dataA dataB
 *
 *   2) JAR 빌드된 경우
 *   CLI 내에 인수 추가
 *   > java -jar project.jar dataA dataB
 * </pre>
 */
@Slf4j
public class CommandLineV1 {
  public static void main(String[] args) {
    for (String arg : args) {
      log.info("arg: {}", arg);
      // IDE
    }
  }
}
