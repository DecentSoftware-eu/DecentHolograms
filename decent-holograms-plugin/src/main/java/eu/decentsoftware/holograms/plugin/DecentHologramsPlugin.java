package eu.decentsoftware.holograms.plugin;

import eu.decentsoftware.holograms.core.DecentHologramsAPI;
import org.bukkit.plugin.java.JavaPlugin;

public class DecentHologramsPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		DecentHologramsAPI.onEnable(this);
	}

	@Override
	public void onDisable() {
		DecentHologramsAPI.onDisable();
	}

}
