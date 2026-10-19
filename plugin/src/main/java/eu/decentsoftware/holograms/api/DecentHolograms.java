package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.features.FeatureManager;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.api.listeners.PlayerListener;
import eu.decentsoftware.holograms.api.listeners.WorldListener;
import eu.decentsoftware.holograms.api.utils.BungeeUtils;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.UpdateChecker;
import eu.decentsoftware.holograms.api.utils.event.EventFactory;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.api.utils.tick.Ticker;
import eu.decentsoftware.holograms.display.DisplayClickService;
import eu.decentsoftware.holograms.display.DisplayEntityRegistry;
import eu.decentsoftware.holograms.display.DisplayModule;
import eu.decentsoftware.holograms.event.DecentHologramsReloadEvent;
import eu.decentsoftware.holograms.integration.IntegrationAvailabilityService;
import eu.decentsoftware.holograms.nms.DecentHologramsNmsPacketListener;
import eu.decentsoftware.holograms.nms.NmsPacketListenerService;
import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import eu.decentsoftware.holograms.platform.api.capability.MinecraftFeature;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatform;
import eu.decentsoftware.holograms.platform.bukkit.BukkitPlatformAdapter;
import eu.decentsoftware.holograms.platform.bukkit.BukkitPlatformBootstrap;
import eu.decentsoftware.holograms.platform.bukkit.player.BukkitPlayerListener;
import eu.decentsoftware.holograms.platform.bukkit.player.BukkitPlayerService;
import lombok.Getter;
import lombok.NonNull;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;

import java.io.File;
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
    private ServerPlatform serverPlatform;
    private NmsAdapter nmsAdapter;
    private IntegrationAvailabilityService integrationAvailabilityService;
    private NmsPacketListenerService nmsPacketListenerService;
    private HologramManager hologramManager;
    private CommandManager commandManager;
    private FeatureManager featureManager;
    private AnimationManager animationManager;
    private Ticker ticker;
    private DisplayModule displayModule;
    private boolean updateAvailable;

    DecentHolograms(@NonNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    void enable(@NonNull BukkitPlatformBootstrap bootstrap) {
        this.nmsAdapter = bootstrap.getNmsAdapter();
        this.serverPlatform = bootstrap.getServerPlatform();
        Settings.reload(plugin);
        Lang.reload(plugin);
        BungeeUtils.init(plugin);

        BukkitPlatformAdapter platformAdapter = bootstrap.getPlatformAdapter();
        S.initialize(platformAdapter.getScheduler(), platformAdapter.getPlayerService());

        PluginManager pluginManager = Bukkit.getPluginManager();
        this.integrationAvailabilityService = new IntegrationAvailabilityService(plugin, pluginManager);
        this.integrationAvailabilityService.initialize();
        this.ticker = new Ticker();
        this.hologramManager = new HologramManager(this);
        this.commandManager = new CommandManager(plugin.getServer());
        this.featureManager = new FeatureManager();
        this.animationManager = new AnimationManager(this);
        DisplayEntityRegistry displayEntityRegistry = new DisplayEntityRegistry();
        DisplayClickService displayClickService = null;
        if (platformAdapter.getCapabilities().supports(MinecraftFeature.DISPLAY_ENTITIES)) {
            this.displayModule = new DisplayModule(plugin, animationManager, platformAdapter, displayEntityRegistry, nmsAdapter);
            this.displayModule.initialize();
            displayClickService = this.displayModule.getDisplayClickService();
        }
        DecentHologramsNmsPacketListener nmsPacketListener = new DecentHologramsNmsPacketListener(hologramManager, displayClickService);
        this.nmsPacketListenerService = new NmsPacketListenerService(plugin, nmsAdapter, nmsPacketListener);

        pluginManager.registerEvents(new PlayerListener(this), this.plugin);
        pluginManager.registerEvents(new WorldListener(hologramManager), this.plugin);
        BukkitPlayerService playerService = (BukkitPlayerService) platformAdapter.getPlayerService();
        pluginManager.registerEvents(new BukkitPlayerListener(playerService), this.plugin);

        setupMetrics();
        checkForUpdates();
    }

    void disable() {
        if (this.displayModule != null) {
            this.displayModule.shutdown();
        }
        this.nmsPacketListenerService.shutdown();
        this.featureManager.destroy();
        this.hologramManager.destroy();
        this.animationManager.destroy();
        this.ticker.destroy();

        for (Hologram hologram : Hologram.getCachedHolograms()) {
            hologram.destroy();
        }

        this.integrationAvailabilityService.shutdown();
        BungeeUtils.destroy();
    }

    /**
     * Reload the plugin, this method also calls the reload event.
     *
     * @see DecentHologramsReloadEvent
     */
    public void reload() {
        Settings.reload(plugin);
        Lang.reload(plugin);

        this.animationManager.reload();
        this.hologramManager.reload();
        this.featureManager.reload();
        if (this.displayModule != null) {
            this.displayModule.reload();
        }

        EventFactory.fireReloadEvent();
    }

    private void setupMetrics() {
        Metrics metrics = new Metrics(this.plugin, 12797);
        metrics.addCustomChart(new SingleLineChart("holograms", () -> Hologram.getCachedHolograms().size()));
        if (displayModule != null) {
            metrics.addCustomChart(new SingleLineChart("total_displays", this::getTotalDisplays));
        }
    }

    private int getTotalDisplays() {
        return displayModule.getDisplayService().getRegisteredDisplays().size();
    }

    private void checkForUpdates() {
        if (!Settings.CHECK_FOR_UPDATES) {
            return;
        }

        UpdateChecker updateChecker = new UpdateChecker(96927);
        updateChecker.getVersion(ver -> {
            String currentVersion = getPlugin().getDescription().getVersion();
            if (Common.isVersionHigher(currentVersion, ver)) {
                Lang.sendUpdateMessage(Bukkit.getConsoleSender());
                this.updateAvailable = true;
            }
        });
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
