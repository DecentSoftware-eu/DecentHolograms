package eu.decentsoftware.holograms.api;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.utils.config.ConfigValue;
import eu.decentsoftware.holograms.utils.config.Configuration;
import eu.decentsoftware.holograms.utils.config.DoubleConfigValue;
import eu.decentsoftware.holograms.utils.config.IntegerConfigValue;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Map;

@UtilityClass
public class Settings {

	private static final DecentHolograms PLUGIN = DecentHologramsProvider.getDecentHolograms();
	public static final Configuration CONFIG = new Configuration(PLUGIN.getPlugin(), "config.yml");

	public static final ConfigValue<Boolean> CHECK_UPDATES = new ConfigValue<>(CONFIG, false, "update-checker", true);

	public static final ConfigValue<String> DEFAULT_TEXT = new ConfigValue<>(CONFIG, false, "defaults.text", "Blank Line");
	public static final ConfigValue<String> DEFAULT_ORIGIN = new ConfigValue<>(CONFIG, false, "defaults.origin", "UP");

	public static final DoubleConfigValue DEFAULT_HEIGHT_TEXT = new DoubleConfigValue(CONFIG, false, "defaults.height.text", 0.3D, 0.0D, 2.5D);
	public static final DoubleConfigValue DEFAULT_HEIGHT_ICON = new DoubleConfigValue(CONFIG, false, "defaults.height.icon", 0.6D, 0.0D, 2.5D);
	public static final DoubleConfigValue DEFAULT_HEIGHT_HEAD = new DoubleConfigValue(CONFIG, false, "defaults.height.head", 0.75D, 0.0D, 2.5D);
	public static final DoubleConfigValue DEFAULT_HEIGHT_SMALLHEAD = new DoubleConfigValue(CONFIG, false, "defaults.height.smallhead", 0.6D, 0.0D, 2.5D);

	public static final IntegerConfigValue DEFAULT_DISPLAY_RANGE = new IntegerConfigValue(CONFIG, false, "defaults.display-range", 64, 8, 64);
	public static final IntegerConfigValue DEFAULT_UPDATE_RANGE = new IntegerConfigValue(CONFIG, false, "defaults.update-range", 64, 8, 64);
	public static final IntegerConfigValue DEFAULT_UPDATE_INTERVAL = new IntegerConfigValue(CONFIG, false, "defaults.update-interval", 20, 1, 1200);

	// ========================================= //

	private static final Map<String, ConfigValue<?>> VALUES = Maps.newHashMap();

	static {
		try {
			Field[] fields = Settings.class.getFields();
			for (Field field : fields) {
				if (field.getType().isAssignableFrom(ConfigValue.class)) {
					VALUES.put(field.getName(), (ConfigValue<?>) field.get(null));
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		Settings.reload();
	}

	/**
	 * Reload all Settings
	 */
	public static void reload() {
		CONFIG.reload();
		for (ConfigValue<?> value : VALUES.values()) {
			value.updateValue();
		}
	}

}
