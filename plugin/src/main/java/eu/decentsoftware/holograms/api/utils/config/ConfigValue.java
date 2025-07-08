package eu.decentsoftware.holograms.api.utils.config;

public class ConfigValue<T> {

	protected final boolean setDefault;
	protected final String path;
	protected final T defaultValue;
	protected T value;

	public ConfigValue(boolean setDefault, String path, T defaultValue) {
		this.setDefault = setDefault;
		this.path = path;
		this.defaultValue = defaultValue;
	}

	public ConfigValue(String path, T defaultValue) {
		this(true, path, defaultValue);
	}

	@SuppressWarnings("unchecked")
	public void updateValue(FileConfig config) {
		if (!config.contains(path)) {
			value = defaultValue;
			if (setDefault) {
				config.set(path, defaultValue);
				config.saveData();
			}
		} else {
			Object o = config.get(path);
			try {
				value = (T) o;
			} catch (Exception e) {
				e.printStackTrace();
				value = defaultValue;
			}
		}
	}

	public String getPath() {
		return path;
	}

	public T getDefaultValue() {
		return defaultValue;
	}

	public T getValue() {
		return value == null ? defaultValue : value;
	}

	public void setValue(T value) {
		this.value = value;
	}

}
