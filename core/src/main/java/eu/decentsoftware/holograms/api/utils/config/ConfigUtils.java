package eu.decentsoftware.holograms.api.utils.config;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;

/**
 * General configuration utilities.
 *
 * @author ZorTik
 */
@UtilityClass
public final class ConfigUtils {

    /**
     * Merges missing keys from the 'from' section into the 'to' section.
     *
     * @param from the source configuration section
     * @param to the target configuration section
     * @return true if any changes were made, false otherwise
     */
    public static boolean merge(ConfigurationSection from, ConfigurationSection to) {
        boolean changed = false;
        for (String key : from.getKeys(false)) {
            if (!to.contains(key)) {
                to.set(key, from.get(key));

                changed = true;
            } else if (from.isConfigurationSection(key) && to.isConfigurationSection(key)) {
                if (merge(from.getConfigurationSection(key), to.getConfigurationSection(key))) {
                    changed = true;
                }
            }
        }

        return changed;
    }
}
