package eu.decentsoftware.holograms.api.managers;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;

/**
 * This interface represents a Manager for handling holograms.
 */
public interface HologramManager {

	/**
	 * Spawn a temporary line that is going to disappear after the given duration.
	 * @param location Location of the line.
	 * @param content Content of the line.
	 * @param duration Duration to disappear after. (in ticks)
	 * @return The Hologram Line.
	 */
	HologramLine spawnTemporaryHologramLine(Location location, String content, long duration);

	/**
	 * Reload this manager and all the holograms.
	 */
	void reload();

	/**
	 * Destroy this manager and all the holograms.
	 */
	void destroy();

	/**
	 * Show all registered holograms for the given player.
	 * @param player Given player.
	 */
	void showAll(Player player);

	/**
	 * Hide all registered holograms for the given player.
	 * @param player Given player.
	 */
	void hideAll(Player player);

	/**
	 * Check whether a hologram with the given name is registered in this manager.
	 * @param name Name of the hologram.
	 * @return Boolean whether a hologram with the given name is registered in this manager.
	 */
	boolean containsHologram(String name);

	/**
	 * Register a new hologram.
	 * @param hologram New hologram.
	 * @return The new hologram or null if it wasn't registered successfully.
	 */
	Hologram registerHologram(Hologram hologram);

	/**
	 * Get hologram by name.
	 * @param name Name of the hologram.
	 * @return The hologram or null if it wasn't found.
	 */
	Hologram getHologram(String name);

	/**
	 * Remove hologram by name.
	 * @param name Name of the hologram.
	 * @return The hologram or null if it wasn't found.
	 */
	Hologram removeHologram(String name);

	/**
	 * Get the names of all registered holograms.
	 * @return Set of the names of all registered holograms.
	 */
	Set<String> getHologramNames();

	/**
	 * Get all registered holograms.
	 * @return Collection of all registered holograms.
	 */
	Collection<Hologram> getHolograms();

}
