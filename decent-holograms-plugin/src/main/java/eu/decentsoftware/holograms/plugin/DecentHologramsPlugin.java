package eu.decentsoftware.holograms.plugin;

import eu.decentsoftware.holograms.core.DecentHologramsAPI;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.CustomChart;
import org.bukkit.plugin.java.JavaPlugin;

public class DecentHologramsPlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		DecentHologramsAPI.onEnable(this);
		Metrics metrics = new Metrics(this, 12797);
		CustomChart customChart;
	}

	@Override
	public void onDisable() {
		DecentHologramsAPI.onDisable();
	}

}
