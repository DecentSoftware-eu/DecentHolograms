package eu.decentsoftware.holograms.shared.reflect;

import java.lang.reflect.Field;

public class ReflectField<T> {

    private final Class<?> parentClass;
    private final String fieldName;
    private Field field;

    public ReflectField(Class<?> clazz, String fieldName) {
        this.parentClass = clazz;
        this.fieldName = fieldName;
    }

    /**
     * Get the value of the field.
     *
     * @param instance Instance of the parent class. (null if the field is static)
     * @return The value of the field.
     * @throws DecentHologramsReflectException If the value could not be retrieved.
     */
    public T get(Object instance) {
        initializeField();
        try {
            return (T) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new DecentHologramsReflectException("Could not get value of field '" + fieldName + "' in class "
                    + parentClass.getName(), e);
        }
    }

    /**
     * Set the value of the field.
     *
     * @param instance Instance of the parent class. (null if the field is static)
     * @param value    The new value.
     * @throws DecentHologramsReflectException If the value could not be set.
     */
    public void set(Object instance, T value) {
        initializeField();
        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            throw new DecentHologramsReflectException("Could not set value of field '" + fieldName + "' in class "
                    + parentClass.getName(), e);
        }
    }

    private void initializeField() {
        if (field != null) {
            return;
        }
        try {
            field = findField(parentClass, fieldName);
            field.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new DecentHologramsReflectException("Could not find field '" + fieldName + "' in class "
                    + parentClass.getName(), e);
        }
    }

    private Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        Field foundField;
        try {
            foundField = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            foundField = clazz.getField(fieldName);
        }
        return foundField;
    }

}
