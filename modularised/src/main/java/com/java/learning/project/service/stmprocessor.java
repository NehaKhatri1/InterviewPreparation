package com.java.learning.project.service;

import com.java.learning.project.dao.EapAttributeDao;
import com.java.learning.project.hive.hive;

import java.sql.SQLException;

import static com.java.learning.project.hive.hive.readFromHive;



public class stmprocessor {

    public static void main(String args[]) throws SQLException {

        Session spark =sparkSessionInitializer(); // initialise spark session in method here if needed
        EapAttributeDao eapAttributeDao= new EapAttributeDao();  // General rule service classes (there can be 2 or more ) calls to dao classes which inturn calls to model claases to return objects of that model class.
        eapAttributeDao.viewTable();

        dataset<Raw> ds1=readFromHive(Session spark,String target);

        enrichDs();

        hive.writeToHive();

    }

    private static void sparkSessionInitializer() {
        return spark.builder().appName("sparksqlapp").getOrCreate();
    }

    private static void enrichDs() {

    }



}
