package eu.decentsoftware.holograms.plugin;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unused")
public class DecentHologramsPlugin {

    private boolean unsupportedServerVersion = false;

    private void onLoad(JavaPlugin plugin) {
        if (Version.CURRENT == null) {
            unsupportedServerVersion = true;
            return;
        }

        DecentHologramsAPI.onLoad(plugin);
    }

    public DecentHolograms onEnable(JavaPlugin plugin) {
        onLoad(plugin);
        if (unsupportedServerVersion) {

            Bukkit.getPluginManager().disablePlugin(plugin);
            return null;
        }

        DecentHologramsAPI.onEnable();

        return DecentHologramsAPI.get();
    }

    public void onDisable() {
        if (unsupportedServerVersion) {
            return;
        }

        DecentHologramsAPI.onDisable();
    }

}
