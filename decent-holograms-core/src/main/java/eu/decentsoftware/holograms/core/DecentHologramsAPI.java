package eu.decentsoftware.holograms.core;

import com.google.common.base.Preconditions;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsProvider;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.commands.DecentPluginCommand;
import eu.decentsoftware.holograms.api.managers.FeatureManager;
import eu.decentsoftware.holograms.api.managers.HologramManager;
import eu.decentsoftware.holograms.api.managers.PlayerManager;
import eu.decentsoftware.holograms.api.nms.NMSAdapter;
import eu.decentsoftware.holograms.core.commands.HologramsCommand;
import eu.decentsoftware.holograms.core.features.DamageDisplayFeature;
import eu.decentsoftware.holograms.core.features.HealingDisplayFeature;
import eu.decentsoftware.holograms.core.listeners.PlayerListener;
import eu.decentsoftware.holograms.core.managers.DefaultFeatureManager;
import eu.decentsoftware.holograms.core.managers.DefaultHologramManager;
import eu.decentsoftware.holograms.core.managers.DefaultPlayerManager;
import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.utils.reflect.ReflectionUtil;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

@Getter
public class DecentHologramsAPI implements DecentHolograms {

	public static void onEnable(JavaPlugin plugin) {
		Preconditions.checkNotNull(plugin, "Plugin cannot be null");
		DecentHologramsAPI decentHologramsAPI = new DecentHologramsAPI(plugin);
		DecentHologramsProvider.setImplementation(decentHologramsAPI);
		decentHologramsAPI.enable();
	}

	public static void onDisable() {
		DecentHologramsProvider.setImplementation(null);
	}

	// ==================================== //

	private final JavaPlugin plugin;
	private HologramManager hologramManager;
	private FeatureManager featureManager;
	private PlayerManager playerManager;
	private NMSAdapter nMSAdapter;
	private DecentPluginCommand command;

	@FieldNameConstants.Exclude
	private boolean start = true;

	/*
	 *	Constructors
	 */

	public DecentHologramsAPI(JavaPlugin plugin) {
		this.plugin = plugin;
	}

	/*
	 *	General Methods
	 */

	private void enable() {
		this.setupNMSAdapter();

		// If there was a problem, shutdown.
		if (!start) {
			Common.log(Level.SEVERE, "There was an error enabling DecentHologramsAPI, shutting down!");
			Bukkit.getPluginManager().disablePlugin(getPlugin());
			return;
		}
		Settings.reload();
		Lang.reload();

		hologramManager = new DefaultHologramManager();
		playerManager = new DefaultPlayerManager();
		featureManager = new DefaultFeatureManager();
		featureManager.registerFeature(new DamageDisplayFeature());
		featureManager.registerFeature(new HealingDisplayFeature());

		command = new HologramsCommand();
		command.register(getPlugin());

		Bukkit.getPluginManager().registerEvents(new PlayerListener(), getPlugin());

		// Setup metrics
		new Metrics(getPlugin(), 12797);
	}

	public void disable() {
		if (start) {
			hologramManager.destroy();
			featureManager.destroy();
			playerManager.destroy();
		}
	}

	@Override
	public void reload() {
		Settings.reload();
		Lang.reload();

		hologramManager.reload();
		featureManager.reload();
		playerManager.reload();
	}

	/*
	 *	Setup Methods
	 */

	private void setupNMSAdapter() {
		String version = ReflectionUtil.getVersion();
		try {
			Class<?> clazz = Class.forName("eu.decentsoftware.holograms.nms." + version + ".NMSAdapter");
			this.nMSAdapter = (NMSAdapter) clazz.getDeclaredConstructor().newInstance();
			Common.log("Using NMS version: %s", version);
		} catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
			Common.log(Level.SEVERE, "Your server version (%s) is not supported...", version);
			this.start = false;
		}
	}

}
