package eu.decentsoftware.holograms.utils.config;

public class IntegerConfigValue {

	private final Configuration config;
	private final boolean setDefault;
	private final String path;
	private final int defaultValue;
	private final int min, max;
	private int value;

	public IntegerConfigValue(Configuration config, boolean setDefault, String path, int defaultValue, int min, int max) {
		this.config = config;
		this.setDefault = setDefault;
		this.path = path;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;

		this.updateValue();
	}

	public IntegerConfigValue(Configuration config, String path, int defaultValue, int min, int max) {
		this(config, true, path, defaultValue, min, max);
	}

	public void updateValue() {
		if (!config.contains(path)) {
			value = defaultValue;
			if (setDefault) {
				config.set(path, defaultValue);
				config.saveData();
				config.reload();
			}
		} else {
			try {
				value = config.getInt(path);
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

	public int getDefaultValue() {
		return defaultValue;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = limit(value);
	}

	private int limit(int value) {
		return Math.min(Math.max(value, min), max);
	}

}
