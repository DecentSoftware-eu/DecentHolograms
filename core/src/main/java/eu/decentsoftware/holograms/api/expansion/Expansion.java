package eu.decentsoftware.holograms.api.expansion;

import eu.decentsoftware.holograms.api.context.AppContext;
import eu.decentsoftware.holograms.api.expansion.context.ExpansionContext;
import eu.decentsoftware.holograms.api.expansion.requirement.ExpansionRequirement;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

/**
 * Represents an app expansion for the additional functionality.
 *
 * @author ZorTik
 */
public interface Expansion {

    /**
     * Called when the expansion is being enabled.
     *
     * @param context the expansion context
     * @param appContext the application context
     */
    default void onEnable(ExpansionContext context, AppContext appContext) {
    }

    /**
     * Called when the expansion is being disabled.
     *
     * @param context the expansion context
     * @param appContext the application context
     */
    default void onDisable(ExpansionContext context, AppContext appContext) {
    }

    /**
     * Returns the unique identifier of this expansion.
     *
     * @return the unique identifier
     */
    String getId();

    /**
     * Returns the name of this expansion.
     *
     * @return the name of the expansion
     */
    String getName();

    /**
     * Returns a brief description of this expansion.
     *
     * @return the description of the expansion
     */
    String getDescription();

    /**
     * Returns the author of this expansion.
     *
     * @return the author of the expansion
     */
    String getAuthor();

    /**
     * Returns the version of this expansion.
     *
     * @return the version of the expansion
     */
    String getVersion();

    /**
     * Returns the plugin that provides this expansion.
     *
     * @return the plugin instance, or null if not applicable
     */
    @Nullable
    default Plugin getPlugin() {
        return null;
    }

    /**
     * Applies default configuration settings to the provided settings section.
     * The provided settings section is ALWAYS empty and the purpose of this method is to
     * populate it to the desired default state.
     *
     * @param settings the configuration section to apply defaults to
     */
    default void applyConfigurationDefaults(ConfigurationSection settings) {
        // No default settings
    }

    /**
     * Returns a collection of requirements that must be met for this expansion to be able to be enabled.
     *
     * @return a collection of expansion requirements
     */
    default Collection<? extends ExpansionRequirement> getRequirements() {
        return Collections.emptyList();
    }

    /**
     * Returns the default 'enabled' state of this expansion.
     *
     * @return true if the expansion is enabled by default, false otherwise
     */
    default boolean isEnabledByDefault() {
        return true;
    }
}
