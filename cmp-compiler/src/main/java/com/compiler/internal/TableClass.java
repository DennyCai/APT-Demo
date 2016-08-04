package com.compiler.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Cai on 2016/7/22.
 */
public class TableClass {
    private String packageName;
    private String className;
    private String genPackageName;
    private String genClassName;
    private Map<String,BeanField> fields;
    private String createTable;
    private String tableName;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getCreateTable() {
        return createTable;
    }

    public void setCreateTable(String createTable) {
        this.createTable = createTable;
    }

    public String getGenPackageName() {
        return genPackageName;
    }

    public void setGenPackageName(String genPackageName) {
        this.genPackageName = genPackageName;
    }

    public String getGenClassName() {
        return genClassName;
    }

    public void setGenClassName(String genClassName) {
        this.genClassName = genClassName;
    }

    public Map<String, BeanField> getFields() {
        return fields;
    }

    public void setFields(Map<String, BeanField> fields) {
        this.fields = fields;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


    @Override
    public String toString() {
        return "TableClass{" +
                "packageName='" + packageName + '\'' +
                ", className='" + className + '\'' +
                ", fields=" + fields +
                '}';
    }

    public List<BeanField> toFields() {
        List<BeanField> fields = new ArrayList<>();
        if(fields!=null){
            for (Map.Entry<String,BeanField> entry:this.fields.entrySet()) {
                fields.add(entry.getValue());
            }
            return fields;
        }
        return null;
    }
}
