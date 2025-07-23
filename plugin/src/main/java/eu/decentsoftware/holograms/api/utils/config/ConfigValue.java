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

	public T getValue() {
		return value == null ? defaultValue : value;
	}



}
