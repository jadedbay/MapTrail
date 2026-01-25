package io.github.jadedbay.Util;

import com.hypixel.hytale.logger.HytaleLogger;
import io.github.jadedbay.MapTrailPlugin;

import java.lang.reflect.Field;

public class ReflectionUtil {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public static <T> T getPrivateField(Object object, String fieldName, Class<T> fieldType, Class<?> sourceClass) {
        try {
            Field field = sourceClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return fieldType.cast(field.get(object));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.atSevere().log(e.getMessage());
            return null;
        }
    }
}
