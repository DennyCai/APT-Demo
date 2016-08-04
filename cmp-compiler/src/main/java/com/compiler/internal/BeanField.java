package com.compiler.internal;

/**
 * Created by Cai on 2016/8/3.
 */
public class BeanField extends BaseField{

    private String setter;
    private String getter;
    private String mapper;


    public String getMapper() {
        return mapper;
    }

    public void setMapper(String mapper) {
        this.mapper = mapper;
    }

    public String getSetter() {
        return setter;
    }

    public void setSetter(String setter) {
        this.setter = setter;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

}
