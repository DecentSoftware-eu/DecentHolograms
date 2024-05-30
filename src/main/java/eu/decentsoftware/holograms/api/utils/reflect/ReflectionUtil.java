package eu.decentsoftware.holograms.api.utils.reflect;

import eu.decentsoftware.holograms.api.utils.Log;
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
    private static final String CRAFTBUKKIT_PACKAGE = Bukkit.getServer().getClass().getPackage().getName();
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
                // The field is not accessible
            }
        }
        return false;
    }

    /**
     * Get the value of a field with the given name from the given object.
     * <p>
     * If the field is not found, or is not accessible, this method will return null.
     *
     * @param object    The object to get the field from.
     * @param fieldName The name of the field to get.
     * @param <T>       The type of the field.
     * @return The value of the field, or null if the field was not found.
     */
    @Nullable
    public static <T> T getFieldValue(final @NotNull Object object, final @NotNull String fieldName) {
        Class<?> clazz = object instanceof Class<?> ? (Class<?>) object : object.getClass();
        Field field = getCachedField(clazz, fieldName);
        if (field != null) {
            try {
                return (T) field.get(object);
            } catch (IllegalAccessException ignored) {
                // The field is not accessible
            }
        }
        return null;
    }

    /**
     * Get a field with the given name from the given class.
     * <p>
     * If the field is not found, this method will return null.
     *
     * @param clazz     The class to get the field from.
     * @param fieldName The name of the field to get.
     * @return The field, or null if the field was not found.
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

    /**
     * Get the NMS version of the server.
     * <p>
     * This is the version of the server, such as v1_8_R3. This is used for reflection.
     *
     * @return The version of the server.
     */
    public static String getVersion() {
        if (version == null) {
            version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        }
        return version;
    }

    /**
     * Get a class by class path. The classPath should be the full path, including the package.
     *
     * @param classPath The path of the class to get.
     * @return The class, or null if the class was not found.
     */
    @Nullable
    public static Class<?> getClass(final @NotNull String classPath) {
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            Log.error("Failed to get class: %s", e, classPath);
            return null;
        }
    }

    /**
     * Get a class from the {@code net.minecraft} package. The classPath should be the full path to the class,
     * including the package without the {@code net.minecraft} prefix.
     * <p>
     * This is a shortcut for {@code getClass("net.minecraft." + classPath)}.
     * <p>
     * If you are looking for the old {@code net.minecraft.server.<VERSION>} package,
     * use {@link #getNMSClass(String)}.
     *
     * @param classPath The path of the class to get.
     * @return The class, or null if the class was not found.
     * @see #getNMSClass(String)
     */
    @Nullable
    public static Class<?> getNMClass(final @NotNull String classPath) {
        try {
            return Class.forName("net.minecraft." + classPath);
        } catch (ClassNotFoundException e) {
            Log.error("Failed to get net.minecraft class: %s", e, classPath);
            return null;
        }
    }

    /**
     * Get a class from the {@code net.minecraft.server.<VERSION>} package. The classPath should be the full path to the class,
     * including the package without the {@code net.minecraft.server.<VERSION>} prefix.
     * <p>
     * This is a shortcut for {@code getClass("net.minecraft.server." + getVersion() + "." + classPath)}.
     * <p>
     * If you are looking for the new {@code net.minecraft} package, use {@link #getNMClass(String)}.
     *
     * @param classPath The path of the class to get.
     * @return The class, or null if the class was not found.
     */
    @Nullable
    public static Class<?> getNMSClass(final @NotNull String classPath) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + classPath);
        } catch (ClassNotFoundException e) {
            Log.error("Failed to get net.minecraft.server class: %s", e, classPath);
            return null;
        }
    }

    /**
     * Get a class from the {@code org.bukkit.craftbukkit.<VERSION>} package. The classPath should be the full path to the class,
     * including the package without the {@code org.bukkit.craftbukkit.<VERSION>} prefix.
     * <p>
     * This is a shortcut for {@code getClass("org.bukkit.craftbukkit." + getVersion() + "." + name)}.
     *
     * @param classPath The path of the class to get.
     * @return The class, or null if the class was not found.
     */
    @Nullable
    public static Class<?> getObcClass(final @NotNull String classPath) {
        try {
            return Class.forName(CRAFTBUKKIT_PACKAGE + "." + classPath);
        } catch (ClassNotFoundException e) {
            Log.error("Failed to get org.bukkit.craftbukkit class: %s", e, classPath);
            return null;
        }
    }

}
