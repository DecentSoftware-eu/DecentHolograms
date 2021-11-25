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
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DamageDisplayFeature extends AbstractFeature implements Listener {

	private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();
	private int duration = 40;
	private String appearance = "&c+ {damage}";

	public DamageDisplayFeature() {
		super("damage_display");
		this.reload();
	}

	@Override
	public void reload() {
		this.disable();

		Configuration config = Settings.CONFIG;
		enabled = config.getBoolean("damage-display.enabled", enabled);
		duration = config.getInt("damage-display.duration", duration);
		appearance = config.getString("damage-display.appearance", appearance);

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
		enabled = false;
	}

	@Override
	public void destroy() {
		this.disable();
	}

	@Override
	public String getDescription() {
		return "Spawn a temporary hologram displaying damage.";
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageEvent e) {
		if (e.isCancelled()) return;
		Entity entity = e.getEntity();
		double damage = e.getFinalDamage();

		Location location = LocationUtils.randomizeLocation(entity.getLocation().clone().add(0, 1, 0));
		String text = String.format(appearance.replace("{damage}", "%.1f"), damage);
		PLUGIN.getHologramManager().spawnTemporaryHologramLine(location, text, duration);
	}

}
