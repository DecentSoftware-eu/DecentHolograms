package eu.decentsoftware.holograms.api.managers;

import eu.decentsoftware.holograms.api.player.DecentPlayer;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

/**
 * This interface represents a Manager for handling players.
 */
public interface PlayerManager {

	/**
	 * Reload this manager and all the players.
	 */
	void reload();

	/**
	 * Destroy this manager and all the players.
	 */
	void destroy();

	/**
	 * Check whether a player with the given UUID is registered in this manager.
	 * @param uuid UUID of the player.
	 * @return Boolean whether a player with the given UUID is registered in this manager.
	 */
	boolean containsPlayer(@NonNull final UUID uuid);

	/**
	 * Create a new DecentPlayer in this manager.
	 * @param player The bukkit player.
	 * @return The new DecentPlayer or null if it wasn't created successfully.
	 */
	DecentPlayer createPlayer(@NonNull final Player player);

	/**
	 * Get DecentPlayer by UUID.
	 * @param uuid UUID of the player.
	 * @return The DecentPlayer or null if it wasn't found.
	 */
	DecentPlayer getPlayer(@NonNull final UUID uuid);

	/**
	 * Remove DecentPlayer by UUID.
	 * @param uuid UUID of the player.
	 * @return The DecentPlayer or null if it wasn't found.
	 */
	DecentPlayer removePlayer(@NonNull final UUID uuid);

	/**
	 * Get the UUIDs of all registered DecentPlayers.
	 * @return Set of the UUIDs of all registered DecentPlayers.
	 */
	Set<UUID> getPlayerUuids();

	/**
	 * Get all registered DecentPlayers.
	 * @return Set of all registered DecentPlayers.
	 */
	Collection<DecentPlayer> getPlayers();

}
