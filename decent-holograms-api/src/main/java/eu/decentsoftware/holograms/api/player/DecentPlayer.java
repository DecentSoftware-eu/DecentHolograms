package eu.decentsoftware.holograms.api.player;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * This class represents a profile of player.
 */
public class DecentPlayer {

	private final UUID uuid;
	private final String name;

	/*
	 *	Constructors
	 */

	public DecentPlayer(final Player player) {
		Validate.notNull(player);
		this.uuid = player.getUniqueId();
		this.name = player.getName();
	}

	/**
	 * Destroy this DecentPlayer.
	 */
	public void destroy() {}

	/**
	 * Get the UUID of this player.
	 * @return the UUID of this player.
	 */
	public UUID getUniqueId() {
		return uuid;
	}

	/**
	 * Get the name of this player.
	 * @return the name of this player.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the bukkit instance of this player.
	 * @return the bukkit instance of this player.
	 */
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

	public boolean isOnline() {
		return getPlayer() != null;
	}

	public static DecentPlayer getByUUID(UUID uuid) {
		return DecentHologramsAPI.get().getPlayerManager().getPlayer(uuid);
	}

}
