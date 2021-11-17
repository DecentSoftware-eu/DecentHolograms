package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.features.FeatureManager;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.api.menu.MenuListener;
import eu.decentsoftware.holograms.api.nms.NMS;
import eu.decentsoftware.holograms.api.nms.PacketListener;
import eu.decentsoftware.holograms.api.player.PlayerListener;
import eu.decentsoftware.holograms.api.player.PlayerManager;
import eu.decentsoftware.holograms.api.utils.BungeeUtils;
import lombok.Getter;
import org.apache.commons.lang.Validate;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

@Getter
public final class DecentHolograms {

	private final JavaPlugin plugin;
	private HologramManager hologramManager;
	private CommandManager commandManager;
	private FeatureManager featureManager;
	private PlayerManager playerManager;
	private AnimationManager animationManager;
	private PacketListener packetListener;
	private File dataFolder;

	/*
	 *	Constructors
	 */

	DecentHolograms(JavaPlugin plugin) {
		Validate.notNull(plugin);
		this.plugin = plugin;
	}

	/*
	 *	General Methods
	 */

	protected void enable() {
		NMS.init();
		Settings.reload();
		Lang.reload();

		hologramManager = new HologramManager();
		commandManager = new CommandManager();
		featureManager = new FeatureManager();
		playerManager = new PlayerManager();
		animationManager = new AnimationManager();
		packetListener = new PacketListener();

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(this), plugin);
		pm.registerEvents(new MenuListener(), plugin);

		// Setup metrics
		Metrics metrics = new Metrics(plugin, 12797);
		metrics.addCustomChart(new SingleLineChart("holograms", () -> Hologram.getCachedHolograms().size()));

		BungeeUtils.init();
	}

	protected void disable() {
		packetListener.destroy();
		hologramManager.destroy();
		featureManager.destroy();
		playerManager.destroy();

		BungeeUtils.destroy();
	}

	public void reload() {
		Settings.reload();
		Lang.reload();

		hologramManager.reload();
		featureManager.reload();
		playerManager.reload();
	}

	public File getDataFolder() {
		if (dataFolder == null) {
			dataFolder = new File("plugins/DecentHolograms");
		}
		return dataFolder;
	}

}
