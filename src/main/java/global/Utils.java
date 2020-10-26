package global;

import java.util.Random;

public class Utils {

  /**
   * Creates random number
   * @param min lower bound (inclusive)
   * @param max upper bound (inclusive)
   * @return number
   */
  public static int getRandomNumberInRange(int min, int max) {

    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    Random r = new Random();
    return r.nextInt((max - min) + 1) + min;
  }

 /**
   * Creates random number not equal to exclude
   * @param min lower bound (inclusive)
   * @param max upper bound (inclusive)
   * @return number
   */
  public static int getRandomNumberInRange(int min, int max, int exclude) {

    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    //prevent endless loop
    if (min + 1 >= max && min < exclude && max > exclude ) {
      throw new IllegalArgumentException("max must be greater than min");
    }


    Random r = new Random();
    int random;
    do{
      random = r.nextInt((max - min) + 1) + min;
    }while (random == exclude);
    return random;
  }
}
