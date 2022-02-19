package eu.decentsoftware.holograms.plugin.features;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.features.AbstractFeature;
import eu.decentsoftware.holograms.api.utils.config.Configuration;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class HealingDisplayFeature extends AbstractFeature implements Listener {

	private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();
	private int duration = 40;
	private String appearance = "&a+ {heal}";

	public HealingDisplayFeature() {
		super("healing_display");
		this.reload();
	}

	@Override
	public void reload() {
		this.disable();

		Configuration config = Settings.CONFIG;
		enabled = config.getBoolean("healing-display.enabled", enabled);
		duration = config.getInt("healing-display.duration", duration);
		appearance = config.getString("healing-display.appearance", appearance);

		if (enabled) {
			this.enable();
		}
	}

	@Override
	public void enable() {
		JavaPlugin javaPlugin = PLUGIN.getPlugin();
		javaPlugin.getServer().getPluginManager().registerEvents(this, javaPlugin);
		this.enabled = true;
	}

	@Override
	public void disable() {
		HandlerList.unregisterAll(this);
		this.enabled = false;
	}

	@Override
	public void destroy() {
		this.disable();
	}

	@Override
	public String getDescription() {
		return "Spawn a temporary hologram displaying heals.";
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityRegainHealthEvent e) {
		if (e.isCancelled()) return;
		Entity entity = e.getEntity();
		double heal = e.getAmount();

		if (heal > 0d) {
			Location location = LocationUtils.randomizeLocation(entity.getLocation().clone().add(0, 1, 0));
			String text = String.format(appearance.replace("{heal}", "%.1f"), heal);
			PLUGIN.getHologramManager().spawnTemporaryHologramLine(location, text, duration);
		}
	}

}
