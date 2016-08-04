package com.compiler.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by Cai on 2016/7/22.
 */
public enum AccessModifier {
    PUBLIC("public"), PROTECTED("protected"), PRIVATE("private"), DEFAULT("default");

    private String mMod;

    private AccessModifier(String mod) {
        mMod = mod;
    }

    public String get() {
        return mMod;
    }

    public static AccessModifier valueOf(int modifier){
        switch (modifier){
            case Modifier.PROTECTED:
                return PROTECTED;
            case Modifier.PUBLIC:
                return PUBLIC;
            case Modifier.PRIVATE:
                return PRIVATE;
            default:
                return DEFAULT;
        }
    }
}
