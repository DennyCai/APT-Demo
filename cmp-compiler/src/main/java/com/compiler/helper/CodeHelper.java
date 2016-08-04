package com.compiler.helper;

import java.lang.reflect.Field;

import javax.annotation.processing.Filer;

/**
 * Created by Cai on 2016/7/22.
 */
public class CodeHelper {

    private final String mPackageName;
    private final String mClassName;
    private AccessModifier mModifier = AccessModifier.DEFAULT;
    private Filer mFiler;

    public static CodeHelper New(Filer filer, String packageName, String className){
        if(filer!=null&&packageName!=null&&className!=null)
            return new CodeHelper(filer,packageName, className);
        else
            throw new UnsupportedOperationException("Filer:"+String.valueOf(filer)
                    +",packageName:"+String.valueOf(packageName)
                    +"className:"+String.valueOf(className));
    }

    private CodeHelper(Filer filer,String packageName,String className){
        mFiler = filer;
        mPackageName = packageName;
        mClassName = className;
    }

    public CodeHelper setClassAccessModifiers(AccessModifier modifiers){
        mModifier = modifiers;
        return this;
    }


    public String build(){
        StringBuilder builder = new StringBuilder();
        buildClassWarpper(builder);
        buildFields(builder);
        buildMethod(builder);
        closeClass(builder);
        return builder.toString();
    }

    private void buildMethod(StringBuilder builder) {

    }

    private void buildFields(StringBuilder builder) {

    }

    private void closeClass(StringBuilder builder) {
        builder.append("}");
    }

    private void buildClassWarpper(StringBuilder builder) {

    }

}
