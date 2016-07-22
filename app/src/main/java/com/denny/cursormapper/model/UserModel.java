package com.denny.cursormapper.model;

import com.cursormapper.annotation.Columns;
import com.cursormapper.annotation.Table;
import com.denny.cursormapper.table.UserColums;

/**
 * Created by hasee on 2016/4/26.
 */
@Table(name="")
public class UserModel {
    @Columns(name = UserColums.ID)
    private long id;
    @Columns(name = UserColums.NAME)
    private String name;
    @Columns(name = UserColums.AGE)
    private int age;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "UserModel[id:"+id+",name:"+name+",age:"+age+"]";
    }
}
