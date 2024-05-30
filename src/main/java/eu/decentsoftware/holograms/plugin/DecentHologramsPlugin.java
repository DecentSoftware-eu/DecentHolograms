package eu.decentsoftware.holograms.plugin;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.plugin.commands.HologramsCommand;
import eu.decentsoftware.holograms.plugin.features.DamageDisplayFeature;
import eu.decentsoftware.holograms.plugin.features.HealingDisplayFeature;
import org.bukkit.plugin.java.JavaPlugin;

public class DecentHologramsPlugin extends JavaPlugin {

	@Override
	public void onLoad() {
		DecentHologramsAPI.onLoad(this);
	}

	@Override
	public void onEnable() {
		DecentHologramsAPI.onEnable();

		DecentHolograms decentHolograms = DecentHologramsAPI.get();
		decentHolograms.getFeatureManager().registerFeature(new DamageDisplayFeature());
		decentHolograms.getFeatureManager().registerFeature(new HealingDisplayFeature());

		CommandManager commandManager = decentHolograms.getCommandManager();
		DecentCommand mainCommand = new HologramsCommand();
		commandManager.setMainCommand(mainCommand);
		commandManager.registerCommand(mainCommand);
	}

	@Override
	public void onDisable() {
		DecentHologramsAPI.onDisable();
	}

}
