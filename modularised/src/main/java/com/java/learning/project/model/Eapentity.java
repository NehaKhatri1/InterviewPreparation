package com.java.learning.project.model;

/*
   key:-  The no of pojos classes depends upon the tables/schemas from where u want to read data . if there are 3 tables 3 model/pojo classes will
           be there and 3 dao classes will be there w.r.t. supporting operations in each table .
*/

public class Eapentity {

    private String entityid;
    private String entityName;


    public String getEntityid() {
        return entityid;
    }

    public void setEntityid(String entityid) {
        this.entityid = entityid;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }





}
