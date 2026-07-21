package eu.decentsoftware.holograms.platform.bukkit;

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.platform.api.resource.SaveResourceService;
import org.bukkit.plugin.java.JavaPlugin;

public class BukkitSaveResourceService implements SaveResourceService {

    private final JavaPlugin plugin;

    public BukkitSaveResourceService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void saveResource(String resourceName, boolean overwrite) {
        try {
            plugin.saveResource(resourceName, overwrite);
            Log.info("Saved resource '%s'.", resourceName);
        } catch (IllegalArgumentException e) {
            Log.error("Failed to save resource '%s'.", e, resourceName);
        }
    }
}
