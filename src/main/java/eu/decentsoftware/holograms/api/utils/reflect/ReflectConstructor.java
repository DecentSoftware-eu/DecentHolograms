package eu.decentsoftware.holograms.api.utils.reflect;

import eu.decentsoftware.holograms.api.utils.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectConstructor {

    private final Class<?> clazz;
    private final Class<?>[] parameterTypes;

    private Constructor<?> constructor;

    public ReflectConstructor(Class<?> clazz, Class<?>... parameterTypes) {
        this.clazz = clazz;
        this.parameterTypes = parameterTypes;
    }

    private void init() {
        if (constructor != null) return;
        try {
            constructor = clazz.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            Log.error("Failed to find constructor for class %s with parameter types %s", clazz.getName(), parameterTypes);
        }
    }

    public <T> T newInstance(Object... args) {
        this.init();

        Object object = null;
        try {
            object = constructor.newInstance(args);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            Log.error("Failed to create new instance of class %s with parameter types %s", clazz.getName(), parameterTypes);
        }
        return object == null ? null : (T) object;
    }

}
