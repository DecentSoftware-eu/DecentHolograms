package eu.decentsoftware.holograms.utils.config;

public class ConfigValue<T> {

	protected final Configuration config;
	protected final boolean setDefault;
	protected final String path;
	protected final T defaultValue;
	protected T value;

	public ConfigValue(Configuration config, boolean setDefault, String path, T defaultValue) {
		this.config = config;
		this.setDefault = setDefault;
		this.path = path;
		this.defaultValue = defaultValue;

		this.updateValue();
	}

	public ConfigValue(Configuration config, String path, T defaultValue) {
		this(config, true, path, defaultValue);
	}

	@SuppressWarnings("unchecked")
	public void updateValue() {
		if (!config.contains(path)) {
			value = defaultValue;
			if (setDefault) {
				config.set(path, defaultValue);
				config.saveData();
				config.reload();
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

	public Configuration getConfig() {
		return config;
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
