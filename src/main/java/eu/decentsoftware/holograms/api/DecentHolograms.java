package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.features.FeatureManager;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.api.nms.NMS;
import eu.decentsoftware.holograms.api.nms.PacketListener;
import eu.decentsoftware.holograms.api.listeners.PlayerListener;
import eu.decentsoftware.holograms.api.utils.scheduler.SchedulerAdapter;
import eu.decentsoftware.holograms.api.utils.scheduler.adapters.BukkitSchedulerAdapter;
import eu.decentsoftware.holograms.api.utils.scheduler.adapters.FoliaSchedulerAdapter;
import eu.decentsoftware.holograms.api.utils.BungeeUtils;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.DExecutor;
import eu.decentsoftware.holograms.api.utils.UpdateChecker;
import eu.decentsoftware.holograms.api.utils.event.EventFactory;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectionUtil;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.api.utils.tick.Ticker;
import eu.decentsoftware.holograms.api.listeners.WorldListener;
import eu.decentsoftware.holograms.event.DecentHologramsReloadEvent;
import lombok.Getter;
import lombok.NonNull;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    private final SchedulerAdapter scheduler;
    private HologramManager hologramManager;
    private CommandManager commandManager;
    private FeatureManager featureManager;
    private AnimationManager animationManager;
    private PacketListener packetListener;
    private Ticker ticker;
    private boolean updateAvailable;

    /*
     *	Constructors
     */

    DecentHolograms(@NonNull JavaPlugin plugin) {
        this.plugin = plugin;
        this.scheduler = FoliaSchedulerAdapter.isSupported() ? new FoliaSchedulerAdapter(plugin) : new BukkitSchedulerAdapter(plugin);
    }

    /*
     *	General Methods
     */

    void load() {
        // Check if NMS version is supported
        if (Version.CURRENT == null) {
            Common.log(Level.SEVERE, "Unsupported server version: " + ReflectionUtil.getVersion());
            Common.log(Level.SEVERE, "Plugin will be disabled.");
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    void enable() {
        NMS.init();
        Settings.reload();
        Lang.reload();
        DExecutor.init(3);

        this.ticker = new Ticker();
        this.hologramManager = new HologramManager(this);
        this.commandManager = new CommandManager();
        this.featureManager = new FeatureManager();
        this.animationManager = new AnimationManager(this);
        this.packetListener = new PacketListener(this);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this.plugin);
        pm.registerEvents(new WorldListener(this), this.plugin);

        // Setup metrics
        Metrics metrics = new Metrics(this.plugin, 12797);
        metrics.addCustomChart(new SingleLineChart("holograms", () -> Hologram.getCachedHolograms().size()));

        // Setup update checker
        if (Settings.CHECK_FOR_UPDATES) {
            UpdateChecker updateChecker = new UpdateChecker(getPlugin(), 96927);
            updateChecker.getVersion(ver -> {
                if (Common.isVersionHigher(ver)) {
                    Lang.sendUpdateMessage(Bukkit.getConsoleSender());
                    this.updateAvailable = true;
                }
            });
        }

        BungeeUtils.init();
    }

    void disable() {
        this.packetListener.destroy();
        this.featureManager.destroy();
        this.hologramManager.destroy();
        this.animationManager.destroy();
        this.ticker.destroy();

        for (Hologram hologram : Hologram.getCachedHolograms()) {
            hologram.destroy();
        }

        BungeeUtils.destroy();
        DExecutor.shutdownNow();
    }

    /**
     * Reload the plugin, this method also calls the reload event.
     *
     * @see DecentHologramsReloadEvent
     */
    public void reload() {
        Settings.reload();
        Lang.reload();

        this.animationManager.reload();
        this.hologramManager.reload();
        this.featureManager.reload();

        EventFactory.handleReloadEvent();
    }

    @Contract(pure = true)
    public File getDataFolder() {
        return plugin.getDataFolder();
    }

    @Contract(pure = true)
    public Logger getLogger() {
        return plugin.getLogger();
    }

}
