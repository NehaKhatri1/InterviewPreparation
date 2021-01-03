package java8Basics;

/*The following program demonstrates the use of the Optional class.  */

import java.util.Optional;

public class OptionalClass {

  public static void main(String[] args) {
    String[] str = new String[10];
    System.out.println("str is " + str.toString());
    Optional<String> checkNull = Optional.ofNullable(str[5]);
    if (checkNull.isPresent()) {
      String word = str[5].toLowerCase();
      System.out.print(word);
    } else
      System.out.println("string is null");
  }
}
