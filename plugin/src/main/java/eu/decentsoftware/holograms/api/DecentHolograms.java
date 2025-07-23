package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.api.listeners.PlayerListener;
import eu.decentsoftware.holograms.api.listeners.WorldListener;
import eu.decentsoftware.holograms.api.utils.BungeeUtils;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.api.utils.tick.Ticker;
import eu.decentsoftware.holograms.nms.NmsAdapterFactory;
import eu.decentsoftware.holograms.nms.NmsPacketListenerService;

import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the main class of DecentHolograms. It contains all the methods
 * and fields that are used to manage DecentHolograms. You can get the instance
 * of this class by using {@link DecentHologramsAPI#get()}.
 *
 * @author d0by
 * @see DecentHologramsAPI
 */
@Getter
public final class DecentHolograms {

    private final JavaPlugin plugin;
    private NmsAdapter nmsAdapter;
    private NmsPacketListenerService nmsPacketListenerService;
    private HologramManager hologramManager;

    private Ticker ticker;

    DecentHolograms(@NonNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    void enable() {
        Log.setLogger(plugin.getLogger());
        initializeNmsAdapter();
        Settings.reload();

        this.ticker = new Ticker();
        this.hologramManager = new HologramManager();


        this.nmsPacketListenerService = new NmsPacketListenerService(plugin, nmsAdapter);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this.plugin);
        pm.registerEvents(new WorldListener(hologramManager), this.plugin);

        BungeeUtils.init();
    }

    void disable() {
        this.nmsPacketListenerService.shutdown();
        this.hologramManager.destroy();

        this.ticker.destroy();

        for (Hologram hologram : Hologram.getCachedHolograms()) {
            hologram.destroy();
        }

        BungeeUtils.destroy();
    }

    private void initializeNmsAdapter() {
        try {
            nmsAdapter = new NmsAdapterFactory().createNmsAdapter(Version.CURRENT);
            Log.info("Initialized NMS adapter for %s (%s).", Version.CURRENT.name(), Version.CURRENT_MINECRAFT_VERSION);
            return;
        } catch (Exception e) {
            Log.error("Unknown error loading an NMS adapter for " + Version.CURRENT, e);
        }
        Log.error("The plugin will now be disabled.");
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

}
