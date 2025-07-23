package eu.decentsoftware.holograms.api.utils.reflect;

import eu.decentsoftware.holograms.api.utils.Log;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

@UtilityClass
public class ReflectionUtil {

    private static String version;

    /**
     * Find a field with in a class with a specific type.
     * <p>
     * If the field is not found, this method will return null.
     *
     * @param clazz     The class to get the field from.
     * @param type      The class type of the field.
     * @return The field, or null if the field was not found.
     */
    public static Field findField(Class<?> clazz, Class<?> type) {
        if (clazz == null) return null;

        Field[] methods = clazz.getDeclaredFields();
        for (Field method : methods) {
            if (!method.getType().equals(type)) continue;

            method.setAccessible(true);
            return method;
        }
        return null;
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
     * Get a class from the {@code net.minecraft.server.<VERSION>} package. The classPath should be the full path to the class,
     * including the package without the {@code net.minecraft.server.<VERSION>} prefix.
     * <p>
     * This is a shortcut for {@code getClass("net.minecraft.server." + getVersion() + "." + classPath)}.
     * <p>

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
     * Check if a class exists.
     *
     * @param classPath The class path.
     * @return True if the class exists, false otherwise.
     */
    public static boolean checkClassExists(final @NotNull String classPath) {
        try {
            Class.forName(classPath);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
