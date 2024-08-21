package hello.utils;

import java.util.Random;

public class ThreadUtils {

  public static void sleep(int min, int randomBound) {
    try {
      Thread.sleep(min + new Random().nextInt(randomBound));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
