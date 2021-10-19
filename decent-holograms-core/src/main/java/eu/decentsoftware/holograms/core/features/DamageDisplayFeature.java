package eu.decentsoftware.holograms.core.features;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsProvider;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.features.IFeature;
import eu.decentsoftware.holograms.utils.config.Configuration;
import eu.decentsoftware.holograms.utils.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DamageDisplayFeature implements IFeature, Listener {

	private static final DecentHolograms PLUGIN = DecentHologramsProvider.getDecentHolograms();
	private boolean enabled;
	private int duration;
	private String appearance;

	public DamageDisplayFeature() {
		this.enabled = false;
		this.duration = 40;
		this.appearance = "&c+ {damage}";
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
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getName() {
		return "damage_display";
	}

	@Override
	public String getDescription() {
		return "Spawn a temporary hologram displaying damage.";
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		Entity entity = e.getEntity();
		double damage = e.getFinalDamage();

		Location location = LocationUtils.randomizeLocation(entity.getLocation().clone().add(0, 1, 0));
		String text = appearance.replace("{damage}", String.valueOf((int) damage));
		PLUGIN.getHologramManager().spawnTemporaryHologramLine(location, text, duration);
	}

}
