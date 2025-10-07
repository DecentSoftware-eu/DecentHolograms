package eu.decentsoftware.holograms.api.context;

import eu.decentsoftware.holograms.api.utils.reflect.Version;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents the application context.
 *
 * @author ZorTik
 */
public interface AppContext {

    /**
     * Gets the server version.
     *
     * @return the server version
     */
    Version getServerVersion();

    /**
     * Gets the plugin instance.
     *
     * @return the plugin instance
     */
    JavaPlugin getPlugin();
}
