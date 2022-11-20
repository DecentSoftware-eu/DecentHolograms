package eu.decentsoftware.holograms.api;

import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

public final class DecentHologramsAPI {

	private static DecentHolograms implementation;

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
	}

	/**
	 * Disable DecentHologramsAPI.
	 */
	public static void onDisable() {
		if (implementation == null) return;
		implementation.disable();
		implementation = null;
	}

	/**
	 * Check whether DecentHologramsAPI is currently running.
	 */
	public static boolean isRunning() {
		return implementation != null;
	}

	/**
	 * Get the instance of running DecentHolograms.
	 *
	 * @return the instance of running DecentHolograms.
	 */
	public static DecentHolograms get() {
		if (implementation == null) {
			throw new IllegalStateException("There is no running instance of DecentHologramsAPI, enable it first.");
		}
		return implementation;
	}

}
