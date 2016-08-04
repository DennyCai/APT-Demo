package com.compiler.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cai on 2016/8/4.
 */
public class UtilsBean {
    private String packageName;
    private List<CheckMethod> checkMethods;

    public void addCheckMethod(CheckMethod method){
        if(checkMethods==null){
            checkMethods = new ArrayList<>();
        }
        if(method!=null)
            checkMethods.add(method);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<CheckMethod> getCheckMethods() {
        return checkMethods;
    }

    public void setCheckMethods(List<CheckMethod> checkMethods) {
        this.checkMethods = checkMethods;
    }
}
