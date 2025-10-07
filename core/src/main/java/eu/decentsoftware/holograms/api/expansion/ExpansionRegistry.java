package eu.decentsoftware.holograms.api.expansion;

import eu.decentsoftware.holograms.api.expansion.exception.PluginNotLinkedException;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public interface ExpansionRegistry {

    /**
     * Registers (and activates) a new expansion.
     *
     * @param expansion the expansion to register
     * @throws IllegalArgumentException if an expansion with the same ID is already registered
     * @throws PluginNotLinkedException if the expansion's plugin is not linked
     */
    void registerExpansion(Expansion expansion);

    /**
     * Unregisters (and deactivates) an expansion by its ID.
     *
     * @param id the ID of the expansion to unregister
     * @return the unregistered expansion, or null if no expansion with the given ID was found
     */
    @Nullable
    Expansion unregisterExpansion(String id);

    /**
     * Unregisters all expansions associated with a specific plugin.
     *
     * @param plugin the plugin whose expansions should be unregistered
     */
    void unregisterExpansionsForPlugin(Plugin plugin);

    /**
     * Gets an expansion by its ID.
     *
     * @param id the ID of the expansion to get
     * @return the expansion, or null if no expansion with the given ID was found
     */
    @Nullable
    Expansion getExpansion(String id);

    /**
     * Gets a list of all registered expansions.
     *
     * @return a list of all registered expansions
     */
    @Unmodifiable
    List<Expansion> getAllExpansions();
}
