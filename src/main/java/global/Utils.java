package global;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class Utils {

  /**
   * Creates random number
   * 
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
   * 
   * @param min lower bound (inclusive)
   * @param max upper bound (inclusive)
   * @return number
   */
  public static int getRandomNumberInRange(int min, int max, int exclude) {

    if (min >= max) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    // prevent endless loop
    if (min + 1 >= max && min < exclude && max > exclude) {
      throw new IllegalArgumentException("max must be greater than min");
    }

    Random r = new Random();
    int random;
    do {
      random = r.nextInt((max - min) + 1) + min;
    } while (random == exclude);
    return random;
  }

  public static String getCurrentISOTimeString() {
    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
    df.setTimeZone(tz);
    return df.format(new Date());
  }

  public static String getRandomHexString(int numchars){
    Random r = new Random();
    StringBuffer sb = new StringBuffer();
    while(sb.length() < numchars){
        sb.append(Integer.toHexString(r.nextInt()));
    }

    return sb.toString().substring(0, numchars);
}
}
