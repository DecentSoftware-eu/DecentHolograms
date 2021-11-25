package eu.decentsoftware.holograms.api;

import org.bukkit.plugin.java.JavaPlugin;

public final class DecentHologramsAPI {

	private static DecentHolograms implementation;

	/**
	 * Enable DecentHologramsAPI.
	 * @param plugin Plugin from which it's being enabled.
	 */
	public static void onEnable(JavaPlugin plugin) {
		if (implementation != null) return;
		implementation = new DecentHolograms(plugin);
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
	 * <p>
	 *     If you use shaded version of the API, you must enable
	 *     it first using {@link DecentHologramsAPI#onEnable(JavaPlugin)} class.
	 *     Also, don't forget to disable the API in you onDisable method
	 *     when you are done using it. {@link DecentHologramsAPI#onDisable()}
	 * </p>
	 *
	 * @return the instance of running DecentHolograms.
	 */
	public static DecentHolograms get() {
		if (implementation == null) {
			throw new IllegalStateException("There is no running instance of DecentHologramsAPI, enabled it first.");
		}
		return implementation;
	}

}
