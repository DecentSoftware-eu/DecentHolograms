package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.commands.CommandBase;
import eu.decentsoftware.holograms.api.managers.FeatureManager;
import eu.decentsoftware.holograms.api.managers.HologramManager;
import eu.decentsoftware.holograms.api.managers.PlayerManager;
import eu.decentsoftware.holograms.api.nms.NMSAdapter;
import org.bukkit.plugin.java.JavaPlugin;

public interface DecentHolograms {

	/**
	 * Reload DecentHolograms.
	 */
	void reload();

	HologramManager getHologramManager();

	FeatureManager getFeatureManager();

	PlayerManager getPlayerManager();

	NMSAdapter getNMSAdapter();

	CommandBase getCommand();

	JavaPlugin getPlugin();

}
