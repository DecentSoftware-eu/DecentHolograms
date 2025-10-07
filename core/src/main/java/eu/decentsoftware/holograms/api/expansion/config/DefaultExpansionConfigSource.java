package eu.decentsoftware.holograms.api.expansion.config;

import eu.decentsoftware.holograms.api.expansion.Expansion;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.config.ConfigUtils;
import eu.decentsoftware.holograms.api.utils.file.FileUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Default implementation of ExpansionConfigSource.
 * Each expansion's configuration is stored in a separate file within the specified directory.
 *
 * @author ZorTik
 */
public class DefaultExpansionConfigSource implements ExpansionConfigSource {
    private final File configsDirectory;

    public DefaultExpansionConfigSource(File configsDirectory) {
        this.configsDirectory = configsDirectory;
    }

    /**
     * @see ExpansionConfigSource#loadOrCreateConfig(Expansion)
     */
    @Override
    public ExpansionConfig loadOrCreateConfig(Expansion expansion) {
        File configFile = ensureExpansionConfigFile(expansion);

        boolean shouldBeSaved = false;

        ConfigurationSection config = YamlConfiguration.loadConfiguration(configFile);
        if (!config.contains("enabled")) {
            boolean enabledByDefault = expansion.isEnabledByDefault();

            config.set("enabled", enabledByDefault);
            shouldBeSaved = true;
        }

        ConfigurationSection settings;
        if (config.contains("settings")) {
            settings = config.getConfigurationSection("settings");

            if (mergeDefaults(expansion, settings)) {
                shouldBeSaved = true;
            }
        } else {
            settings = config.createSection("settings");
            expansion.applyConfigurationDefaults(settings);

            shouldBeSaved = true;
        }

        ExpansionConfig expansionConfig = new ExpansionConfig(config.getBoolean("enabled"), settings);
        if (shouldBeSaved) {
            saveConfig(expansion, expansionConfig);
        }

        return expansionConfig;
    }

    /**
     * Merges the default configuration values provided by the expansion into the given config section.
     *
     * @param expansion the expansion providing the default values
     * @param config the configuration section to merge defaults into
     * @return true if any changes were made, false otherwise
     */
    private static boolean mergeDefaults(Expansion expansion, ConfigurationSection config) {
        ConfigurationSection defaults = new MemoryConfiguration();
        expansion.applyConfigurationDefaults(defaults);

        return ConfigUtils.merge(defaults, config);
    }

    /**
     * @see ExpansionConfigSource#saveConfig(Expansion, ExpansionConfig)
     */
    @Override
    public void saveConfig(Expansion expansion, ExpansionConfig config) {
        File file = ensureExpansionConfigFile(expansion);

        YamlConfiguration section = YamlConfiguration.loadConfiguration(file);
        section.set("enabled", config.isEnabled());
        section.set("settings", config.getSettings());

        try {
            section.save(file);

            config.setChanged(false);
        } catch (Exception e) {
            Log.error("Could not save config for expansion " + expansion.getId(), e);
        }
    }

    /**
     * Ensure that the configuration file for the given expansion exists.
     * If the file or its parent directory does not exist, they will be created.
     *
     * @param expansion the expansion whose config file is to be ensured
     * @return the ensured configuration file
     */
    private File ensureExpansionConfigFile(Expansion expansion) {
        FileUtils.ensureFolder(configsDirectory);

        File configFile = new File(configsDirectory, expansion.getId() + ".yml");
        FileUtils.ensureFile(configFile);

        return configFile;
    }
}
