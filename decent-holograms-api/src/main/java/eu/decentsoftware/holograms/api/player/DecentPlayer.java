package eu.decentsoftware.holograms.api.player;

import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * This interface represents a profile of player.
 */
public interface DecentPlayer {

	/**
	 * Destroy this DecentPlayer.
	 */
	void destroy();

	/**
	 * Get the UUID of this player.
	 * @return the UUID of this player.
	 */
	UUID getUniqueId();

	/**
	 * Get the name of this player.
	 * @return the name of this player.
	 */
	String getName();

	/**
	 * Get the bukkit instance of this player.
	 * @return the bukkit instance of this player.
	 */
	Player getPlayer();

}
