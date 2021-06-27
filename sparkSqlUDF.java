
import org.apache.commons.codec.StringEncoder;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.api.java.UDF1;

import static org.apache.spark.sql.functions.explode;
import static org.apache.spark.sql.functions.split;

/**
 * Input :-- [{"name": "neha","age": "30"},{"name": "vivek", "age": "33"},{"name": "rekha", "age": "55"},{"name": "aryan", "age": "16"},{"name": "kulbhushan", "age": "62"},{"name": "kanta", "age": "30"}]
 * <p>
 * <p>
 * output :--
 * <p>
 * +---+----------+
 * |age|      name|
 * +---+----------+
 * | 30|      neha|
 * | 33|     vivek|
 * | 55|     rekha|
 * | 16|     aryan|
 * | 62|kulbhushan|
 * | 30|     kanta|
 * +---+----------+
 * <p>
 * root
 * |-- age: string (nullable = true)
 * |-- name: string (nullable = true)
 * <p>
 * <p>
 * +-----+
 * | name|
 * +-----+
 * | neha|
 * |kanta|
 * +-----+
 */


public class sparkSqlExample {

    private static Object StringEncoder;

    public static void main(String[] args) {
    /*SparkSession spark = SparkSession
            .builder()
            .appName("Java Spark SQL basic example")
            .config("spark.master", "local")
            .getOrCreate();
*/
        SparkSession spark = SparkSession.builder().appName("Java Spark SQL basic example")
                .master("local[*]").config("spark.driver.memory", "5g")
                .config("spark.driver.host", "127.0.0.1") // #Machine ip  (localhost worked for me)
                .config("spark.driver.bindAddress", "127.0.0.1") // #Machine ip (localhost worked for me )
                .getOrCreate();


        //  Dataset<Row> df = spark.read().json("examples/src/main/resources/people.json");
        Dataset<Row> df = spark.read().json("/home/osboxes/IntellijProjects/sparkSqlExample/src/resources/people.json");

        // Displays the content of the DataFrame to stdout
        df.show();


        // get the count of df
        System.out.println("total row count in df is ****  " + df.count());  //total row count in df is ****  6

     /*   +---+----------+
                |age|      name|
                +---+----------+
                | 30|      neha|
                | 33|     vivek|
                | 55|     rekha|
                | 16|     aryan|
                | 62|kulbhushan|
                +---+----------+
        */


        df.printSchema();


        // Register the DataFrame as a SQL temporary view
        df.createOrReplaceTempView("people");


        /*--------------------------------------------------------------------------------------------------------------------------------------------*/
        // You can extract a column of a dataframe by its name
        Dataset<Row> sqlDF = spark.sql("SELECT current_timestamp(),name FROM people where age=30");

       // The columns of a row in the result can be accessed by field index
        Encoder<String> stringEncoder = Encoders.STRING();
        Dataset<String> teenagerNamesByIndexDF = sqlDF.map(new MapFunction<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return "name: " + row.getString(1);
            }
        }, stringEncoder);


        System.out.println(" teenagerNamesByIndexDF");

        teenagerNamesByIndexDF.show();



        /*--------------------------------------------------------------------------------------------------------------------------------------------*/

        /*&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&*/
        Dataset<Row> sqlDF1 = spark.sql("SELECT max(age), min(age) FROM people");


        // Prints value of dataframe
        sqlDF1.show();

        /*      +--------+--------+
                |max(age)|min(age)|
                +--------+--------+
                |      62|      16|
                +--------+--------+
        */


        // This doesnt print value of df
        System.out.println("sqlDF1 " + sqlDF1);   // prints sqlDF1 [max(age): string, min(age): string]


        /*&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&*/


  /*  UDF POC    */

      //   Writting 1st UDF
       //  Terminology/fundamental
        // 1.  dataframe == table;
        // 2. register dataframe as tempview   --> df.createTempView("employee")
        // 3. To select columns apply dataframe.sql(select col from employee))
        // 4. udf called as nameofUdf(column)  in select statement .
// 5.  The Results of sql queries are dataframes and support all the normal RDD operations .


        //  Dataset<Row> df = spark.read().json("examples/src/main/resources/people.json");
        Dataset<Row> df10 = spark.read().json("/home/osboxes/IntellijProjects/sparkSqlExample/src/resources/people.json");


        // Register the DataFrame as a SQL temporary view
        df10.createOrReplaceTempView("person");



        spark.udf().register("GettingSurname", new UDF1<String, String>() {
            @Override
            public String call(String fullName) throws Exception {
                String[] strArr = fullName.split(" ");

               return strArr[1];
            }
        }, DataTypes.StringType);


        Dataset<Row> sqlDF10 = spark.sql("SELECT GettingSurname(name) FROM person ");  // call  udf(column) from tempview  ; createtempview from source dataframe & then pass column of dataframe to udf .
        sqlDF10.show();

        /*&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&*/

        //-------------------------------------------------------------------------------------------------------//
      // UDF Example 2 concatinating 2 columns

      // notice UDF2 for new udf
        spark.udf().register("ConcatedColumns", new UDF2<String, String,String>() {
            @Override
            public String call(String column1, String column2) throws Exception {
                String concatedStr = column1+" * * "+column2;

                return concatedStr;
            }
        }, DataTypes.StringType);




        Dataset<Row> sqlDF11 = spark.sql("SELECT ConcatedColumns(name,age) FROM person ");  // call  udf(column) from tempview  ; createtempview from source dataframe & then pass column of dataframe to udf .
        sqlDF11.show();

        //-------------------------------------------------------------------------------------------------------//

       //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&//

     
  // must read very good doc    
  /*
  A Dataset is a strongly typed collection of domain-specific objects that can be transformed in parallel using functional or relational operations. Each Dataset also has an untyped view called a DataFrame, which is a Dataset of Row.
Operations available on Datasets are divided into transformations and actions. Transformations are the ones that produce new Datasets, and actions are the ones that trigger computation and return results. Example transformations include map, filter, select, and aggregate (groupBy). Example actions count, show, or writing data out to file systems.

Datasets are "lazy", i.e. computations are only triggered when an action is invoked. Internally, a Dataset represents a logical plan that describes the computation required to produce the data. When an action is invoked, Spark's query optimizer optimizes the logical plan and generates a physical plan for efficient execution in a parallel and distributed manner. To explore the logical plan as well as optimized physical plan, use the explain function.

To efficiently support domain-specific objects, an Encoder is required. The encoder maps the domain specific type T to Spark's internal type system. For example, given a class Person with two fields, name (string) and age (int), an encoder is used to tell Spark to generate code at runtime to serialize the Person object into a binary structure. This binary structure often has much lower memory footprint as well as are optimized for efficiency in data processing (e.g. in a columnar format). To understand the internal binary representation for data, use the schema function.

There are typically two ways to create a Dataset. The most common way is by pointing Spark to some files on storage systems, using the read function available on a SparkSession.


   val people = spark.read.parquet("...").as[Person]  // Scala
   Dataset<Person> people = spark.read().parquet("...").as(Encoders.bean(Person.class)); // Java
 
Datasets can also be created through transformations available on existing Datasets. For example, the following creates a new Dataset by applying a filter on the existing one:


   val names = people.map(_.name)  // in Scala; names is a Dataset[String]
   Dataset<String> names = people.map((Person p) -> p.name, Encoders.STRING));
 
Dataset operations can also be untyped, through various domain-specific-language (DSL) functions defined in: Dataset (this class), Column, and functions. These operations are very similar to the operations available in the data frame abstraction in R or Python.

To select a column from the Dataset, use apply method in Scala and col in Java.


   val ageCol = people("age")  // in Scala
   Column ageCol = people.col("age"); // in Java
 
Note that the Column type can also be manipulated through its various functions.


   // The following creates a new column that increases everybody's age by 10.
   people("age") + 10  // in Scala
   people.col("age").plus(10);  // in Java
 
A more concrete example in Scala:


   // To create Dataset[Row] using SparkSession
   val people = spark.read.parquet("...")
   val department = spark.read.parquet("...")

   people.filter("age > 30")
     .join(department, people("deptId") === department("id"))
     .groupBy(department("name"), people("gender"))
     .agg(avg(people("salary")), max(people("age")))
 
and in Java:


   // To create Dataset<Row> using SparkSession
   Dataset<Row> people = spark.read().parquet("...");
   Dataset<Row> department = spark.read().parquet("...");

   people.filter(people.col("age").gt(30))
     .join(department, people.col("deptId").equalTo(department.col("id")))
     .groupBy(department.col("name"), people.col("gender"))
     .agg(avg(people.col("salary")), max(people.col("age")));
        // We Can apply map/flatmap like transformation/action to dataframe  & even after converting it to RDD or from one dataframe to anothore datafgrame .
*/
        
       // The Results of sql queries are dataframes and support all the normal RDD operations .

        Dataset<Row> sqlDF12 = spark.sql("SELECT name ,age FROM people");

        JavaRDD<String>  javaRDD=sqlDF12.javaRDD().map(e1->e1.getString(0)+" amd");   // wrong implementation  JavaRDD<String>  javaRDD=sqlDF12.map(e1->e1.getString(0)+" amd");
            javaRDD.foreach(e->System.out.println(e));

          Dataset<String>  df15=  sqlDF12.map(new MapFunction<Row, String>() {
                @Override
                public String call(Row row) throws Exception {
                    return "Name_abc " +row.getString(1);
                }
            },Encoders.STRING());

        df15.show();
        //sqlDF12.map(e->e.length()).
        //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&//
        spark.stop();
    }

}




