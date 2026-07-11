package eu.decentsoftware.holograms.api.utils.reflect;

import eu.decentsoftware.holograms.api.utils.Log;

import java.lang.reflect.Field;

public class ReflectField<T> {

	private final Class<?> clazz;
	private final String name;
	private Field field;

	public ReflectField(Class<?> clazz, String name) {
		this.clazz = clazz;
		this.name = name;
	}

	private void init() throws NoSuchFieldException {
		if (field == null) {
			try {
				field = clazz.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
				field = clazz.getField(name);
			}
			field.setAccessible(true);
		}
	}

	@SuppressWarnings("unchecked")
	public T getValue(Object object) {
		try {
			this.init();
			return (T) field.get(object);
		} catch (Exception e) {
			Log.error("Failed to get field value: %s", e, name);
			return null;
		}
	}
}
