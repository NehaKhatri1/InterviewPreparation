
import org.apache.commons.codec.StringEncoder;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.*;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.api.java.UDF1;

import static org.apache.spark.sql.functions.explode;
import static org.apache.spark.sql.functions.split;

/**
 * Input :--[{"name": "neha khatri","age": "30"},{"name": "vivek kumar", "age": "33"},{"name": "rekha khatri", "age": "55"},{"name": "aryan khatri", "age": "16"},{"name": "kulbhushan kumar", "age": "62"},{"name": "kanta kaushal", "age": "30"}]
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
                return "Name: " + row.getString(1);
            }
        }, stringEncoder);
        teenagerNamesByIndexDF.show();


        System.out.println("sqlDF count is  " + sqlDF.count());
        sqlDF.show();


        //sqlDF.withColumn("name",explode(split($ "name", " "))).show();





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

        spark.stop();
    }

}




