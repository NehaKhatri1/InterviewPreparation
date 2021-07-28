import com.sun.applet2.AppletParameters;
import org.apache.spark.api.java.function.FlatMapFunction2;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import scala.collection.Seq;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.lit;

public class sparkPocDataframe {

    private static Object String;
    private static Object List;

    public static void main(String[] args) {

        SparkSession spark = SparkSession.builder().appName("Java Spark SQL basic example")
                .master("local[*]").config("spark.driver.memory", "5g")
                .config("spark.driver.host", "127.0.0.1") // #Machine ip  (localhost worked for me)
                .config("spark.driver.bindAddress", "127.0.0.1") // #Machine ip (localhost worked for me )
                .getOrCreate();

        spark.sparkContext().setLogLevel("ERROR");

        Dataset<Row> df11 = spark.read().json("/home/osboxes/IntellijProjects/sparkSqlExample/src/resources/employee1.json");

        Dataset<Row> df1 = df11.withColumnRenamed("empid", "source_table_empid").withColumnRenamed("name", "source_table_name").withColumnRenamed("city", "source_table_city");

        df1.show();

        Dataset<Row> df22 = spark.read().json("/home/osboxes/IntellijProjects/sparkSqlExample/src/resources/employee2.json");

        Dataset<Row> df2 = df22.withColumnRenamed("empid", "target_table_empid").withColumnRenamed("name1", "target_table_name1").withColumnRenamed("city", "target_table_city");
        df2.show();


        List<String> listOfDF1Fields = Arrays.stream(df1.schema().fieldNames()).sorted().collect(Collectors.toList());
        List<String> listOfDF2Fields = Arrays.stream(df2.schema().fieldNames()).sorted().collect(Collectors.toList());
        List<String> listOfDF1FieldsSorted = listOfDF1Fields.stream().sorted().collect(Collectors.toList());

        List<String> listOfDF2FieldsSorted = listOfDF2Fields.stream().sorted().collect(Collectors.toList());


        //System.out.println("sorted schema df1 " + listOfDF1FieldsSorted);
        //System.out.println("sorted schema df2 " + listOfDF2FieldsSorted);


       // System.out.println("schema df1 getting element " + listOfDF1FieldsSorted.get(0));
        //System.out.println("schema df2 getting element " + listOfDF2FieldsSorted.get(0));


        if (listOfDF1FieldsSorted.size() != listOfDF2FieldsSorted.size())
            throw new IllegalArgumentException("Cannot combine lists with dissimilar sizes");

        Map<String, String> hasmapOfschemas = new LinkedHashMap<String, String>();
        for (int i = 0; i < listOfDF1FieldsSorted.size(); i++) {
            hasmapOfschemas.put(listOfDF2FieldsSorted.get(i), listOfDF1FieldsSorted.get(i));
        }
        System.out.println("hasmapOfschemas " + hasmapOfschemas);

        //Dataset <Row> joined = df1.join(df2, df1.col("empid").equalTo(df2.col("empid"). as("df2.empid")),"rightouter");
        Dataset<Row> joined = df1.join(df2, df1.col("source_table_empid").equalTo(df2.col("target_table_empid")), "rightouter");

        joined.show();
        //   joined.createOrReplaceTempView("joinedview");


        Dataset<Row> lastDataSet = spark.emptyDataFrame();
        Dataset<Row> finalDataSet = null;
        Dataset<Row> finalDataset1=spark.emptyDataFrame();
       // int Counter =1;
        Dataset<Row> finalDataset2=spark.emptyDataFrame();
        for (Map.Entry<String, String> pair : hasmapOfschemas.entrySet()) {

            // running 2
            Dataset<Row> matchedNameColumn1 = joined.filter(joined.col(pair.getKey()).equalTo(joined.col(pair.getValue()))).select(lit(pair.getKey()).as("target_column_name"), joined.col(pair.getKey()).as("target_column_value"), lit(pair.getValue()).as("source_column_name"), joined.col(pair.getValue()).as("source_column_value"), lit(pair.getValue()).as("empid_column_name"), joined.col("target_table_empid").as("empid_column_value"));//.withColumn("efd",functions.lit(pair.getValue())).select(df1.col(pair.getValue()));
           //Dataset<Row>
                   finalDataset1 = matchedNameColumn1.withColumn("result", functions.lit("pass"));
            System.out.println("finaldataset1 show inside loop");
            finalDataset1.show();
          // lastDataSet = lastDataSet.union(finalDataset1);
            //Counter++;
            Dataset<Row> NotMatchedNameColumn1 = joined.filter(joined.col(pair.getKey()).notEqual(joined.col(pair.getValue()))).select(lit(pair.getKey()).as("target_column_name"), joined.col(pair.getKey()).as("target_column_value"), lit(pair.getValue()).as("source_column_name"), joined.col(pair.getValue()).as("source_column_value"), lit(pair.getValue()).as("empid_column_name"), joined.col("target_table_empid").as("empid_column_value"));//.withColumn("efd",functions.lit(pair.getValue())).select(df1.col(pair.getValue()));
        //   Dataset<Row>
                    finalDataset2 = NotMatchedNameColumn1.withColumn("result", functions.lit("fail"));
            //System.out.println("finaldataset2 show inside");
            //finalDataset2.show();
            //Counter++;
         //   lastDataSet = lastDataSet.union(finalDataset2);

            //   System.out.println("showing finaldataset  now ");
           // finalDataset1 = finalDataset1.union(finalDataset2);//Line 2
         //   System.out.println("last count "+finalDataset1.count()+" "+finalDataset2.count());
            //System.out.println("last count "+finalDataset1.count());
           // System.out.println("lastDataSet count"+lastDataSet.count());

        }
        // joined2.show();

      //  System.out.println("finished");

      //  System.out.println("showing finaldataset ");
        //System.out.println("outside loop last count "+finalDataset1.count()+" "+finalDataset2.count());
        //System.out.println("last counter value outside "+Counter);
        //System.out.println("lastDataSet count"+lastDataSet.count());

        System.out.println("finaldataset1 showing outside loop--");
        finalDataset1.show();


        // finalDataset1.show();

    }


}
