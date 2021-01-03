package java8Basics;

/*Let’s implement a program that demonstrates Lambda Expressions.  */

interface MyInterface {
  void abstract_func(int x, int y);

  default void default_Fun() {
    System.out.println("This is default method");
  }
}

interface addInterface {
  void addition(int a, int b);
}

class functionalInterfaceLambdaexpression {
  public static void main(String args[]) {
    // lambda expression
    MyInterface fobj = (int x, int y) -> System.out.println(x + y);

    System.out.print("The result = ");
    fobj.abstract_func(5, 5);
    fobj.default_Fun();

    // example 2nd of functional interface & lambda exp
    addInterface addInterfaceObj = (int a, int b) -> System.out.println(a + b);
    addInterfaceObj.addition(10, 5);

  }
}

/*
 * 
 * The result = 10 This is default method 15
 */
