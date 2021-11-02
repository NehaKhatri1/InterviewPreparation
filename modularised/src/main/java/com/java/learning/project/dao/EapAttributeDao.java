package com.java.learning.project.dao;

import com.java.learning.project.model.Eapentity;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EapAttributeDao {


    public static List<String> viewTable() throws SQLException {
        List eapentityList = new ArrayList();
        Map<String,String> EapEntityMap= new HashMap<>();


        String query = "select entityid, entityName from resourcestable";
        DriverManager conn;
        try {
               conn= (DriverManager) DBDatabaseManager.getConnection();
                 Statement stmt = ((Connection) conn).createStatement();


            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Eapentity eapentity= new Eapentity();   // one way to use pojos
                eapentity.setEntityid(rs.getString("entityid"));
                eapentity.setEntityName(rs.getString("entityName"));
                eapentityList.add(eapentity);   // adding objects to list


                //or map/list in case u don't want to return object
                EapEntityMap.put(rs.getString("entityid"),rs.getString("entityName"));


           }
            return eapentityList;

        } catch (SQLException e) {
           // JDBCTutorialUtilities.printSQLException(e);
        }
    }

}
