package eu.decentsoftware.holograms.api;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.api.utils.config.ConfigValue;
import eu.decentsoftware.holograms.api.utils.config.Configuration;
import eu.decentsoftware.holograms.api.utils.config.DoubleConfigValue;
import eu.decentsoftware.holograms.api.utils.config.IntegerConfigValue;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Properties;

@UtilityClass
public class Settings {

	private static final String API_VERSION;
	private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
	public static final Configuration CONFIG = new Configuration(DECENT_HOLOGRAMS.getPlugin(), DECENT_HOLOGRAMS.getDataFolder(), "config.yml");

	public static final ConfigValue<Boolean> CHECK_UPDATES = new ConfigValue<>(CONFIG, false, "update-checker", true);
	public static final IntegerConfigValue CLICK_COOLDOWN = new IntegerConfigValue(CONFIG, false, "click-cooldown", 20, 10, 300);

	public static final ConfigValue<String> DEFAULT_TEXT = new ConfigValue<>(CONFIG, false, "defaults.text", "Blank Line");
	public static final ConfigValue<Boolean> DEFAULT_DOWN_ORIGIN = new ConfigValue<>(CONFIG, false, "defaults.down-origin", false);
//	public static final ConfigValue<Boolean> DEFAULT_ALWAYS_FACE_PLAYER = new ConfigValue<>(CONFIG, false, "defaults.always-face-player", true);

	public static final DoubleConfigValue DEFAULT_HEIGHT_TEXT = new DoubleConfigValue(CONFIG, false, "defaults.height.text", 0.3D, 0.0D, 2.5D);
	public static final DoubleConfigValue DEFAULT_HEIGHT_ICON = new DoubleConfigValue(CONFIG, false, "defaults.height.icon", 0.6D, 0.0D, 2.5D);
	public static final DoubleConfigValue DEFAULT_HEIGHT_HEAD = new DoubleConfigValue(CONFIG, false, "defaults.height.head", 0.75D, 0.0D, 2.5D);
	public static final DoubleConfigValue DEFAULT_HEIGHT_SMALLHEAD = new DoubleConfigValue(CONFIG, false, "defaults.height.smallhead", 0.6D, 0.0D, 2.5D);

	public static final IntegerConfigValue DEFAULT_DISPLAY_RANGE = new IntegerConfigValue(CONFIG, false, "defaults.display-range", 48, 8, 48);
	public static final IntegerConfigValue DEFAULT_UPDATE_RANGE = new IntegerConfigValue(CONFIG, false, "defaults.update-range", 48, 8, 48);
	public static final IntegerConfigValue DEFAULT_UPDATE_INTERVAL = new IntegerConfigValue(CONFIG, false, "defaults.update-interval", 20, 1, 1200);

	// ========================================= //

	private static final Map<String, ConfigValue<?>> VALUES = Maps.newHashMap();

	static {
		// Load API version from properties
		Properties versionProperties = new Properties();
		String apiVersion = "UNKNOWN";
		try {
			versionProperties.load(DECENT_HOLOGRAMS.getPlugin().getResource("version.properties"));
			apiVersion = versionProperties.getProperty("version");
		} catch (IOException e) {
			e.printStackTrace();
		}
		API_VERSION = apiVersion;

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

	public static String getAPIVersion() {
		return API_VERSION;
	}

}
