package hello.jdbc.Repository.exception;


/**
 * <h1>주의</h1>
 * <pre>
 *   Exception을 상속받아서 변환할 때는 `기존 Exception`을 반드시 포함하시라.
 *   - 장애가 발새아혹 로그에서 진짜 원인을 찾을 수 없게된다 (심각한 문제)
 * </pre>
 *
 */
public class MyDBDuplicateKeyException extends MyDBException {
  public MyDBDuplicateKeyException() {
  }

  public MyDBDuplicateKeyException(String message) {
    super(message);
  }

  public MyDBDuplicateKeyException(String message, Throwable cause) {
    super(message, cause);
  }

  public MyDBDuplicateKeyException(Throwable cause) {
    super(cause);
  }
}
