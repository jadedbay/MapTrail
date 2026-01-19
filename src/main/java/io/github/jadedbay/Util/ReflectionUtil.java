package io.github.jadedbay.Util;

import java.lang.reflect.Field;

public class ReflectionUtil {
    public static <T> T getPrivateField(Object object, String fieldName, Class<T> fieldType) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return fieldType.cast(field.get(object));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
