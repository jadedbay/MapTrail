package io.github.jadedbay.Util;

import io.github.jadedbay.MapTrailPlugin;

import java.lang.reflect.Field;

public class ReflectionUtil {
    public static <T> T getPrivateField(Object object, String fieldName, Class<T> fieldType, Class<?> sourceClass) {
        try {
            Field field = sourceClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return fieldType.cast(field.get(object));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            MapTrailPlugin.logger().atSevere().log(e.getMessage());
            return null;
        }
    }
}
