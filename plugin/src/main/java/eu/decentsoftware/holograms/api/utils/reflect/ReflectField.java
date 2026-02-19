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

	public ReflectField(Field field) {
		this.field = field;
		this.clazz = field.getDeclaringClass();
		this.name = field.getName();

		this.field.setAccessible(true);
	}

	private void init() throws Exception {
		if (field == null) {
			try {
				field = clazz.getDeclaredField(name);
			} catch (Exception e) {
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

	public void setValue(Object object, Object value) {
		try {
			this.init();
			field.set(object, value);
		} catch (Exception e) {
			Log.error("Failed to set field value: %s", e, name);
		}
	}

}
