package java8Basics;

/*
 Method References
 The Method reference feature introduced in Java 8 is a shorthand notation for Lambda 
 Expressions to call a method of Functional Interface. So each time you use a Lambda 
 Expression to refer a method, you can replace your Lambda Expression with method reference.

 Example of Method Reference.
 */

interface interface_default_2 {
  void display2();
}

class derived_class_2 {

  public void classMethodTwo() {
    System.out.println("Derived class Method");
  }
}

class methodreferences {
  public static void main(String[] args) {
    derived_class_2 obj = new derived_class_2();
    interface_default ref = obj::classMethodTwo;
    ref.display2();
  }
}
