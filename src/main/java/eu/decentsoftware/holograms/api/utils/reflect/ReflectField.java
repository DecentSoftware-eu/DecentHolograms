package eu.decentsoftware.holograms.api.utils.reflect;

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
			e.printStackTrace();
			return null;
		}
	}

	public void setValue(Object object, Object value) {
		try {
			this.init();
			field.set(object, value);
		} catch (Exception ignored) {}
	}

}
