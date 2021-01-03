package java8Basics;

/*Default And Static Methods In Interfaces
 In Java 8, you can add methods in the interface that are not abstract i.e. 
 you can have interfaces with method implementation. 
 You can use the Default and Static keyword to create interfaces with method implementation.
 Default methods mainly enable Lambda Expression functionality.

 Using default methods you can add new functionality to your interfaces in your libraries.
 This will ensure that the code written for the older versions is compatible with those interfaces 
 (binary compatibility).

 Let’s understand the Default Method with an example: */

interface interface_default {

  public void fun(int x);

  default void default_method() {
    System.out.println("I am default method of interface");
  }

  default void default_method1() {
    System.out.println("I am default method  1 of interface");
  }
}

class derived_class implements interface_default {

  @Override
  public void fun(int x) { // TODO Auto-generated method stub
    System.out.println("running fun function" + x);
  }

}

class DefaultAndStaticMethods {
  public static void main(String[] args) {
    derived_class obj1 = new derived_class();
    obj1.default_method();
    obj1.default_method1();

    // example of abstract method which is accepting value using lambda fun. */
    System.out.println("Printing fun's value");
    interface_default interfaceObj = y -> System.out.println(y);
    interfaceObj.fun(10);

    /* example of overridden fun which is accepting value using obj of class(which implements that) */
    obj1.fun(20);
  }
}
/*
 * Output:
 * 
 * I am default method of interface I am default method 1 of interface Printing fun's value 10 running fun function20
 * 
 * 
 * 
 * 
 * We have an interface named “interface_default” with the method default_ method() with a default implementation. Next, we define
 * a class “derived_class” that implements the interface “interface_default”.
 * 
 * Note that we have not implemented any interface methods in this class. Then in the main function, we create an object of class
 * “derived_class” and directly call the “default_method” of the interface without having to define it in the class.
 * 
 * This is the use of default and static methods in the interface. However, if a class wants to customize the default method then
 * you can provide its own implementation by overriding the method.
 */
