package hello.springtx.exception;


public class NotEnoughMoneyException extends Exception{
  public NotEnoughMoneyException(String message) {
    super(message);
  }
}
