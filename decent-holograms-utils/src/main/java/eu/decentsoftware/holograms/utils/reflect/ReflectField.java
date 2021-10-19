package eu.decentsoftware.holograms.utils.reflect;

import java.lang.reflect.Field;

public class ReflectField<T> {

	private final Class<?> clazz;
	private final String name;

	private Field field;

	public ReflectField(Class<?> clazz, String name) {
		this.clazz = clazz;
		this.name = name;
	}

	private void init() throws Exception {
		if (field == null) {
			field = clazz.getDeclaredField(name);
			field.setAccessible(true);
		}
	}

	public T getValue(Object object) {
		try {
			if (field == null) this.init();
			return (T) field.get(object);
		} catch (Exception e) {
			return null;
		}
	}

	public void setValue(Object object, T value) {
		try {
			if (field == null) this.init();
			field.set(object, value);
		} catch (Exception ignored) {}
	}

}
