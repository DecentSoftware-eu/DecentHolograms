package eu.decentsoftware.holograms.api.utils.reflect;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class ReflectionUtil {

    /**
     * Cache for fields to avoid expensive reflection calls every time
     * <p>
     * Class -> (Field Name -> Field)
     */
    private static final Map<Class<?>, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>();
    private static String version;

    /**
     * Set the value of a field with the given name on the given object.
     * <p>
     * If the field is not found, or is not accessible, this method will return false.
     *
     * @param object    The object to set the field on.
     * @param fieldName The name of the field to set.
     * @param value     The value to set the field to.
     * @param <T>       The type of the field and value.
     * @return True if the field was found and set, false otherwise.
     */
    public static <T> boolean setFieldValue(final @NotNull Object object, final @NotNull String fieldName, final @Nullable T value) {
        Class<?> clazz = object.getClass();
        Field field = getCachedField(clazz, fieldName);
        if (field != null) {
            try {
                field.set(object, value);
                return true;
            } catch (IllegalAccessException ignored) {
                // Field is not accessible
            }
        }
        return false;
    }

    /**
     * Get the value of a field with the given name on the given class.
     * <p>
     * If the field is not found, or is not accessible, this method will return null.
     *
     * @param clazz     The class to get the field from.
     * @param fieldName The name of the field to get.
     * @return The value of the field, or null if the field was not found.
     */
    @Nullable
    private static Field getCachedField(final @NotNull Class<?> clazz, final @NotNull String fieldName) {
        Map<String, Field> classFields = FIELD_CACHE.get(clazz);
        if (classFields == null) {
            classFields = new ConcurrentHashMap<>();
            Map<String, Field> existingFields = FIELD_CACHE.putIfAbsent(clazz, classFields);
            if (existingFields != null) {
                classFields = existingFields; // Another thread already added the map
            }
        }

        Field field = classFields.get(fieldName);
        if (field == null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);
                classFields.put(fieldName, field);
            } catch (NoSuchFieldException ignored) {
                // Field does not exist
            }
        }
        return field;
    }

    public static String getVersion() {
        if (version == null) {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }
        return version;
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getNMClass(String name) {
        try {
            return Class.forName("net.minecraft." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getObcClass(String classname) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + classname);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
