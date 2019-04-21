package com.nasserkhosravi.appcomponent.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class JReflectionUtils {

    /**
     * @return list of fields that have certain annotate
     */
    public static <T> ArrayList<T> findFields(Object o, Class<? extends Annotation> certainAnnotate) {
        Class c = o.getClass();
        Field[] declaredFields = c.getDeclaredFields();
        if (declaredFields.length > 0) {
            ArrayList<T> result = new ArrayList<>();
            for (Field field : declaredFields) {
                if (field.isAnnotationPresent(certainAnnotate)) {
                    field.setAccessible(true);
                    try {
                        T v = (T) field.get(o);
                        result.add(v);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }
        return null;
    }

}
