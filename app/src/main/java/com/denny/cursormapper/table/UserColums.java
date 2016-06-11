package com.denny.cursormapper.table;


/**
 * Created by hasee on 2016/4/26.
 */
public interface UserColums{

    String TABLE_NAME = "user";

    String ID = "id";
    String NAME = "name";
    String AGE = "age";

    String[] COLUMNS = new String[]{ID,NAME,AGE};

    String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME+ "("+
            ID + " INTEGER,"+
            NAME + " VARCAHR(32),"+
            AGE + " INTEGER)";

}
