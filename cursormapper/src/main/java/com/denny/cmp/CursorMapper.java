package com.denny.cmp;

import android.database.Cursor;
import android.util.Log;

import com.cursormapper.annotation.Columns;
import com.denny.cmp.utils.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 指针映射Model，使用注解声明映射关系
 */
public class CursorMapper {
    private static Map<String, MethodWapper> mCursorMethod;
    private static Map<String, Method> mSetterCache;

    static {
        mSetterCache = new HashMap<>();
        mCursorMethod = new HashMap<>();
        mCursorMethod.put(int.class.getName(), new MethodWapper() {
            @Override
            Object invoke(Cursor cursor, int index) {
                return cursor.getInt(index);
            }
        });
        mCursorMethod.put(long.class.getName(), new MethodWapper() {
            @Override
            Object invoke(Cursor cursor, int index) {
                return cursor.getLong(index);
            }
        });
        mCursorMethod.put(String.class.getName(), new MethodWapper() {
            @Override
            Object invoke(Cursor cursor, int index) {
                return cursor.getString(index);
            }
        });
        mCursorMethod.put(float.class.getName(), new MethodWapper() {
            @Override
            Object invoke(Cursor cursor, int index) {
                return cursor.getFloat(index);
            }
        });
        mCursorMethod.put(double.class.getName(), new MethodWapper() {
            @Override
            Object invoke(Cursor cursor, int index) {
                return cursor.getDouble(index);
            }
        });
        mCursorMethod.put(short.class.getName(), new MethodWapper() {
            @Override
            Object invoke(Cursor cursor, int index) {
                return cursor.getShort(index);
            }
        });
    }

    public static <T> T load(Cursor cursor, Class<T> t, boolean autoClose) {
        if (cursor == null || cursor.isClosed() || t == null)
            return null;
        try {
            return load(cursor, t);
        } finally {
            if (autoClose)
                cursor.close();
        }

    }

    public static <T> T load(Cursor cursor, Class<T> clz) {
        Field[] fields = clz.getDeclaredFields();
        Log.i("CursorMapper", "Field length:" + fields.length);
        Object obj = null;
        try {
            obj = clz.newInstance();
        } catch (Exception e) {
            Log.e("CursorMapper", "Class: " + clz.getName() + "can not newInstance");
        }
        for (Field f : fields) {
            if(f.isAnnotationPresent(Columns.class)) {
                Columns columns = f.getAnnotation(Columns.class);
                Method setter = loadFieldSetterMethod(f, clz);
                int index = cursor.getColumnIndex(columns.name());
                Object params = getCurosrResult(cursor, index, f.getType());
                try {
                    setter.invoke(obj, params);
                } catch (Exception e) {
                    Log.e("CursorMapper", "Method invoke Execption,Class:" + clz.getName() +
                            " method:" + setter.getName() +
                            " params:" + params.toString());
                }
            }

        }
        return (T) obj;
    }

    private static final Object getCurosrResult(Cursor cursor, int index, Class<?> type) {
        return mCursorMethod.get(type.getName()).invoke(cursor, index);
    }

    private static Method loadFieldSetterMethod(Field f, Class<?> clz) {
        String name = f.getName();
        String setterName = "set" + name;
        setterName = StringUtils.toUpperCaseAt(setterName, 3);
        Log.i("CursorMapper", "Method name:" + setterName);
        Method setter = loadMethodFromCache(clz, setterName);
        if (setter != null)
            return setter;
        try {
            setter = clz.getMethod(setterName, f.getType());
            cacheMethod(clz, setter);
            return setter;
        } catch (NoSuchMethodException e) {
            Log.e("CursorMapper", "can not found setter Method name is" + setterName);
        }
        return null;
    }

    private static final void cacheMethod(Class<?> clz, Method setter) {
        mSetterCache.put(clz.getName() + "." + setter.getName(), setter);
    }

    private static final Method loadMethodFromCache(Class<?> clz, String setterName) {
        return mSetterCache.get(clz.getName() + "." + setterName);
    }

    protected static abstract class MethodWapper {
        abstract Object invoke(Cursor cursor, int index);
    }
}
