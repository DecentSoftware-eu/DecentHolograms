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
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.UpdateChecker;
import eu.decentsoftware.holograms.api.utils.event.EventFactory;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.api.utils.tick.Ticker;
import eu.decentsoftware.holograms.event.DecentHologramsReloadEvent;
import eu.decentsoftware.holograms.nms.DecentHologramsNmsPacketListener;
import eu.decentsoftware.holograms.nms.NmsAdapterFactory;
import eu.decentsoftware.holograms.nms.NmsPacketListenerService;
import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.nms.api.NmsAdapter;
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
    private NmsAdapter nmsAdapter;
    private NmsPacketListenerService nmsPacketListenerService;
    private HologramManager hologramManager;
    private CommandManager commandManager;
    private FeatureManager featureManager;
    private AnimationManager animationManager;
    private Ticker ticker;
    private boolean updateAvailable;

    DecentHolograms(@NonNull JavaPlugin plugin) {
        this.plugin = plugin;
    }

    void enable() {
        Log.setLogger(plugin.getLogger());
        initializeNmsAdapter();
        Settings.reload();
        Lang.reload();

        java.util.Arrays.asList(
       " _____   _    ___   ___   _   _ ",
            "|  __ \\ | |  | | / ____| | \\ | |",
            "| |  | || |__| || |      |  \\| |",
            "| |  | ||  __  || |      | . ` |",
            "| |__| || |  | || |____  | |\\  |",
            "|_____/ |_|  |_| \\_____| |_| \\_|",
            "",
            "DecentHolograms 汉化版加载成功",
            "汉化: postyizhan (驿站忆行)",
            "汉化版仓库: https://github.com/postyizhan/DecentHolograms-CN",
            "交流群: 611076407"
        ).forEach(plugin.getLogger()::info);

        this.ticker = new Ticker();
        this.hologramManager = new HologramManager(this);
        this.commandManager = new CommandManager();
        this.featureManager = new FeatureManager();
        this.animationManager = new AnimationManager(this);
        DecentHologramsNmsPacketListener nmsPacketListener = new DecentHologramsNmsPacketListener(hologramManager);
        this.nmsPacketListenerService = new NmsPacketListenerService(plugin, nmsAdapter, nmsPacketListener);

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(this), this.plugin);
        pm.registerEvents(new WorldListener(hologramManager), this.plugin);

        setupMetrics();
        checkForUpdates();

        BungeeUtils.init();
    }

    void disable() {
        this.nmsPacketListenerService.shutdown();
        this.featureManager.destroy();
        this.hologramManager.destroy();
        this.animationManager.destroy();
        this.ticker.destroy();

        for (Hologram hologram : Hologram.getCachedHolograms()) {
            hologram.destroy();
        }

        BungeeUtils.destroy();
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

        EventFactory.fireReloadEvent();
    }

    private void initializeNmsAdapter() {
        try {
            nmsAdapter = new NmsAdapterFactory().createNmsAdapter(Version.CURRENT);
            Log.info("已初始化 %s (%s) 版本的 NMS 适配器。", Version.CURRENT.name(), Version.CURRENT_MINECRAFT_VERSION);
            return;
        } catch (DecentHologramsNmsException e) {
            Log.error("加载 " + Version.CURRENT + " 版本的NMS适配器时出错: " + e.getMessage(), e);
        } catch (Exception e) {
            Log.error("加载 " + Version.CURRENT + " 版本的NMS适配器时发生未知错误", e);
        }
        Log.error("插件将被禁用。");
        Bukkit.getPluginManager().disablePlugin(plugin);
    }

    private void setupMetrics() {
        Metrics metrics = new Metrics(this.plugin, 12797);
        metrics.addCustomChart(new SingleLineChart("holograms", () -> Hologram.getCachedHolograms().size()));
    }

    private void checkForUpdates() {
        if (!Settings.CHECK_FOR_UPDATES) {
            return;
        }

        UpdateChecker updateChecker = new UpdateChecker(getPlugin(), 96927);
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
