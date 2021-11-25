package eu.decentsoftware.holograms.api.utils.reflect;

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
            e.printStackTrace();
        }
    }

    public <T> T newInstance(Object... args) {
        this.init();

        Object object = null;
        try {
            object = constructor.newInstance(args);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return object == null ? null : (T) object;
    }

}
