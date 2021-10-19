package eu.decentsoftware.holograms.utils.config;

public class DoubleConfigValue {

	private final Configuration config;
	private final boolean setDefault;
	private final String path;
	private final double defaultValue;
	private final double min, max;
	private double value;

	public DoubleConfigValue(Configuration config, boolean setDefault, String path, double defaultValue, double min, double max) {
		this.config = config;
		this.setDefault = setDefault;
		this.path = path;
		this.defaultValue = defaultValue;
		this.min = min;
		this.max = max;

		this.updateValue();
	}

	public DoubleConfigValue(Configuration config, String path, double defaultValue, double min, double max) {
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
				value = config.getDouble(path);
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

	public double getDefaultValue() {
		return defaultValue;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = limit(value);
	}

	private double limit(double value) {
		return Math.min(Math.max(value, min), max);
	}

}
