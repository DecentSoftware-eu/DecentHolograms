package eu.decentsoftware.holograms.api.expansion.config;

import org.bukkit.configuration.ConfigurationSection;

public class ExpansionConfig {
    private final ConfigurationSection settings;

    private boolean enabled;
    private boolean changed = false;

    public ExpansionConfig(boolean enabled, ConfigurationSection settings) {
        this.enabled = enabled;
        this.settings = settings;
    }

    /**
     * Set if the expansion is enabled by config
     *
     * @param enabled true if the expansion is enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Check if the expansion is enabled by config
     *
     * @return true if the expansion is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Get the settings section of the expansion
     *
     * @return the settings section
     */
    public ConfigurationSection getSettings() {
        return settings;
    }

    /**
     * Sets if the config has been changed to indicate that it needs to be saved
     *
     * @param changed true if the config has been changed
     */
    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    /**
     * Check if the config has been changed and needs to be saved
     *
     * @return true if the config has been changed
     */
    public boolean isChanged() {
        return changed;
    }
}
