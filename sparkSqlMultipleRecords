import org.apache.spark.sql.*;
import scala.collection.Seq;

public class sparkSqlMultipleRecords {

    public static void main(String[] args){

        SparkSession spark = SparkSession.builder().appName("Java Spark SQL basic example")
                .master("local[*]").config("spark.driver.memory", "5g")
                .config("spark.driver.host", "127.0.0.1") // #Machine ip  (localhost worked for me)
                .config("spark.driver.bindAddress", "127.0.0.1") // #Machine ip (localhost worked for me )
                .getOrCreate();

        spark.sparkContext().setLogLevel("ERROR");

        Dataset<Row> df1= spark.read().json("/home/osboxes/IntellijProjects/sparkSqlExample/src/resources/sid.json");

       df1.show();
           //  df1.count(df1.col("instrument_sid")).equalto(1).groupby();     //selectExpr("select count(instrument_sid)").show();

       // df1.selectExpr("select count(instrument_sid)").show();

        //df1.select("*", (Seq<String>) df1.col("instrument_sid").equalTo(1)).show();


        //worked
        //Dataset<Row> df2=df1.select("instrument_sid","primary_exchange","is_global_primary").groupBy("instrument_sid").agg(functions.count("instrument_sid"));//.count().equals(1).show();
       // Dataset<Row> df2=df1.select("instrument_sid").groupBy("instrument_sid").agg(functions.count("instrument_sid").as("count_instrument_sid"));//.count().equals(1).show();
       // Dataset<Row> df2=df1.groupBy("instrument_sid").agg(functions.count("instrument_sid").as("count_instrument_sid")).withColumn("result", functions.lit("pass"));//.count().equals(1).show();
        Dataset<Row> df2=df1.select("instrument_sid").groupBy("instrument_sid").agg(functions.count("instrument_sid").as("count_instrument_sid"));//.count().equals(1).show();

        Dataset<Row>  joinedDf =df2.join(df1,"instrument_sid");
        joinedDf.show();

      //  Dataset<Row> df3=df2.filter(df2.col("count_instrument_sid").equalTo(1)).select("*");
//df3.show();

        Dataset<Row> df33=joinedDf.filter(joinedDf.col("count_instrument_sid").equalTo(1)).filter(joinedDf.col("primary_exchange").equalTo("NLS")).select("*");
       // df33.show();

        Dataset<Row> df44=joinedDf.filter(joinedDf.col("count_instrument_sid").gt(1)).filter(joinedDf.col("is_global_primary").equalTo("true")).select("*");

        Dataset<Row> df45=  df33.union(df44);

       df45.show();
    }



}
