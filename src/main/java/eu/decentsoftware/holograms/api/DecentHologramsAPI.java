package eu.decentsoftware.holograms.api;

import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public final class DecentHologramsAPI {

	private static DecentHolograms implementation;
	private static boolean enabled = false;

	public static void onLoad(@NonNull JavaPlugin plugin) {
		if (implementation != null) return;
		implementation = new DecentHolograms(plugin);
		implementation.load();
	}

	/**
	 * Enable DecentHologramsAPI.
	 */
	public static void onEnable() {
		if (implementation == null) return;
		implementation.enable();
		enabled = true;
	}

	/**
	 * Disable DecentHologramsAPI.
	 */
	public static void onDisable() {
		if (implementation == null) return;
		implementation.disable();
		implementation = null;
		enabled = false;
	}

	/**
	 * Check whether DecentHologramsAPI is currently running.
	 */
	public static boolean isRunning() {
		return implementation != null && enabled;
	}

	/**
	 * Get the instance of running DecentHolograms.
	 *
	 * @return the instance of running DecentHolograms.
	 */
	public static DecentHolograms get() {
		if (implementation == null || !enabled) {
			throw new IllegalStateException("DecentHolograms is not running (yet). Do you have DecentHolograms plugin installed?");
		}
		return implementation;
	}

}
