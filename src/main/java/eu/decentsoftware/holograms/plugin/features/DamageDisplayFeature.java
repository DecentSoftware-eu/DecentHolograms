package eu.decentsoftware.holograms.plugin.features;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.features.AbstractFeature;
import eu.decentsoftware.holograms.api.utils.config.FileConfig;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import lombok.NonNull;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

public class DamageDisplayFeature extends AbstractFeature implements Listener {

	private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();
	private int duration = 40;
	private String appearance = "&c{damage}";
	private String criticalAppearance = "&4&lCrit!&4 {damage}";
	private boolean displayForPlayers = true;
	private boolean displayForMobs = true;
	private boolean zeroDamage = false;
	private double heightOffset = 0.0;

	public DamageDisplayFeature() {
		super("damage_display");
		this.reload();
	}

	@Override
	public void reload() {
		this.disable();

		FileConfig config = Settings.getConfig();
		enabled = config.getBoolean("damage-display.enabled", enabled);
		duration = config.getInt("damage-display.duration", duration);
		appearance = config.getString("damage-display.appearance", appearance);
		criticalAppearance = config.getString("damage-display.critical-appearance", criticalAppearance);

		heightOffset = config.getDouble("healing-display.height", heightOffset);

		displayForPlayers = config.getBoolean("damage-display.players", displayForPlayers);
		displayForMobs = config.getBoolean("damage-display.mobs", displayForMobs);
		zeroDamage = config.getBoolean("damage-display.mobs", zeroDamage);

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
		if (e.isCancelled()) {
			return;
		}

		double damage = e.getFinalDamage();

		if (damage <= 0d && !zeroDamage) {
			return;
		}

		Entity entity = e.getEntity();

		if (!(entity instanceof LivingEntity) || entity instanceof ArmorStand) {
			return;
		}

		if (entity instanceof Player && !displayForPlayers) {
			return;
		}

		if (!(entity instanceof Player) && !displayForMobs) {
			return;
		}

		Location location = LocationUtils.randomizeLocation(entity.getLocation().clone().add(0, 1 + heightOffset, 0));
		String appearance;
		Entity damager = (e instanceof EntityDamageByEntityEvent) ? ((EntityDamageByEntityEvent) e).getDamager() : null;
		if (damager instanceof Player && isCritical((Player) damager)) {
			appearance = this.criticalAppearance;
		} else {
			appearance = this.appearance;
		}
		String text = appearance.replace("{damage}", FeatureCommons.formatNumber(damage));
		PLUGIN.getHologramManager().spawnTemporaryHologramLine(location, text, duration);
	}

	/**
	 * Check if the damage dealt, by the given player is critical.
	 *
	 * @param player The player.
	 * @return True if the damage is critical, false otherwise.
	 */
	private boolean isCritical(@NonNull Player player) {
		if (player.getFallDistance() <= 0.0F ||
				player.isOnGround() ||
				player.isInsideVehicle() ||
				player.hasPotionEffect(PotionEffectType.BLINDNESS) ||
				player.getLocation().getBlock().getType() == Material.LADDER ||
				player.getLocation().getBlock().getType() == Material.VINE
		) {
			return false;
		}
		try {
			// Slow Falling is not in all versions
			if (player.hasPotionEffect(PotionEffectType.getByName("SLOW_FALLING"))) {
				return false;
			}
		} catch (Exception ignored) {
		}
		return true;
	}

}
