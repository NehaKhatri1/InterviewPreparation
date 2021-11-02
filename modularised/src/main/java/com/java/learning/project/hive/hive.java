package com.java.learning.project.hive;

import java.util.List;
import java.util.stream.Collector;

public class hive {

    public static Dataset<Raw> readFromHive(session spark, String target){

      Dataset<Raw>  ds=spark.sql("select * from target");
            // do whatever u like here ..
       List<String> filteredDs= ds.filter(x-> x.name).collect(Collector.toList());

       filteredDs.show();

        return filteredDs;
    }


    public static void writeToHive() {
    }
}
