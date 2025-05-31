package eu.decentsoftware.holograms.plugin;

import de.tr7zw.changeme.nbtapi.NBT;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.plugin.commands.HologramsCommand;
import eu.decentsoftware.holograms.plugin.features.DamageDisplayFeature;
import eu.decentsoftware.holograms.plugin.features.HealingDisplayFeature;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DecentHologramsPlugin extends JavaPlugin {

	private boolean unsupportedServerVersion = false;

	@Override
	public void onLoad() {
		if (Version.CURRENT == null) {
			unsupportedServerVersion = true;
			return;
		}

		DecentHologramsAPI.onLoad(this);
	}

	@Override
	public void onEnable() {
		if (unsupportedServerVersion) {
			getLogger().severe("Unsupported server version detected: " + Bukkit.getServer().getVersion());
			getLogger().severe("Plugin will now be disabled.");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		DecentHologramsAPI.onEnable();

		DecentHolograms decentHolograms = DecentHologramsAPI.get();
		decentHolograms.getFeatureManager().registerFeature(new DamageDisplayFeature());
		decentHolograms.getFeatureManager().registerFeature(new HealingDisplayFeature());

		CommandManager commandManager = decentHolograms.getCommandManager();
		DecentCommand mainCommand = new HologramsCommand();
		commandManager.setMainCommand(mainCommand);
		commandManager.registerCommand(mainCommand);
		
		// Enable NBT API to avoid lag spikes when parsing NBT for the first time.
		NBT.preloadApi();
	}

	@Override
	public void onDisable() {
		if (unsupportedServerVersion) {
			return;
		}

		DecentHologramsAPI.onDisable();
	}

}
