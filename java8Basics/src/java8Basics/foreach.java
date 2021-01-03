package java8Basics;

import java.util.ArrayList;
import java.util.List;

/*forEach() Method In Iterable Interface
 Java 8 has introduced a “forEach” method in the interface java.lang.Iterable that can iterate over the elements 
 in the collection. “forEach” is a default method defined in the Iterable interface. It is used by the Collection
 classes that extend the Iterable interface to iterate elements.

 *** The “forEach” method takes the Functional Interface as a single parameter i.e. you can pass Lambda Expression as 
 an argument.

 Example of the forEach() method.  */
public class foreach {
  public static void main(String[] args) {
    List<String> subList = new ArrayList<String>();
    subList.add("Maths");
    subList.add("English");
    subList.add("French");
    subList.add("Sanskrit");
    subList.add("Abacus");
    System.out.println("------------Subject List--------------");
    subList.forEach(sub -> System.out.println(sub)); // method takes the Functional Interface as a single parameter i.e. you can
                                                     // pass Lambda Expression as an argument.
  }
}

/*
 * Output:
 * 
 * forEach method output
 * 
 * ------------Subject List-------------- Maths English French Sanskrit Abacus
 */

/*
 * So we have a collection of subjects i.e. subList. We display the contents of the subList using the forEach method that takes
 * Lambda Expression to print each element.
 */
