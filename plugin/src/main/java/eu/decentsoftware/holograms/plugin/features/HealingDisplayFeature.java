package eu.decentsoftware.holograms.plugin.features;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.features.AbstractFeature;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
	private boolean displayForPlayers = true;
	private boolean displayForMobs = true;
	private double heightOffset = 0.0;

	public HealingDisplayFeature() {
		super("healing_display");
		this.reload();
	}

	@Override
	public void reload() {
		this.disable();


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
	public void onRegain(EntityRegainHealthEvent e) {
		if (e.isCancelled()) {
			return;
		}

		double heal = e.getAmount();
		if (heal <= 0d) {
			return;
		}

		Entity entity = e.getEntity();

		if (!(entity instanceof LivingEntity) || entity instanceof ArmorStand) {
			return;
		}

		LivingEntity livingEntity = (LivingEntity) entity;
		if (livingEntity.getHealth() == livingEntity.getMaxHealth()) {
			return;
		}

		if (entity instanceof Player && !displayForPlayers) {
			return;
		}

		if (!(entity instanceof Player) && !displayForMobs) {
			return;
		}

		Location location = LocationUtils.randomizeLocation(entity.getLocation().clone().add(0, 1 + heightOffset, 0));
		String text = appearance.replace("{heal}", FeatureCommons.formatNumber(heal));
		PLUGIN.getHologramManager().spawnTemporaryHologramLine(location, text, duration);
	}

}
