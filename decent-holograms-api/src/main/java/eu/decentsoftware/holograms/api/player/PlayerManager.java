package eu.decentsoftware.holograms.api.player;

import com.google.common.collect.Maps;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This interface represents a Manager for handling players.
 */
public class PlayerManager {

	private final Map<UUID, DecentPlayer> playerMap = Maps.newHashMap();

	public PlayerManager() {
		this.reload();
	}

	/**
	 * Reload this manager and all the players.
	 */
	public void reload() {
		Bukkit.getOnlinePlayers().forEach((player) -> {
			if (!this.containsPlayer(player.getUniqueId())) {
				this.createPlayer(player);
			}
		});
	}

	/**
	 * Destroy this manager and all the players.
	 */
	public void destroy() {
		for (DecentPlayer decentPlayer : playerMap.values()) {
			decentPlayer.destroy();
		}
		playerMap.clear();
	}

	/**
	 * Check whether a player with the given UUID is registered in this manager.
	 * @param uuid UUID of the player.
	 * @return Boolean whether a player with the given UUID is registered in this manager.
	 */
	public boolean containsPlayer(UUID uuid) {
		Validate.notNull(uuid);
		return playerMap.containsKey(uuid);
	}

	/**
	 * Create a new DecentPlayer in this manager.
	 * @param player The bukkit player.
	 * @return The new DecentPlayer or null if it wasn't created successfully.
	 */
	public DecentPlayer createPlayer(Player player) {
		Validate.notNull(player);
		return playerMap.put(player.getUniqueId(), new DecentPlayer(player));
	}

	/**
	 * Get DecentPlayer by UUID.
	 * @param uuid UUID of the player.
	 * @return The DecentPlayer or null if it wasn't found.
	 */
	public DecentPlayer getPlayer(UUID uuid) {
		Validate.notNull(uuid);
		return playerMap.get(uuid);
	}

	/**
	 * Remove DecentPlayer by UUID.
	 * @param uuid UUID of the player.
	 * @return The DecentPlayer or null if it wasn't found.
	 */
	public DecentPlayer removePlayer(UUID uuid) {
		Validate.notNull(uuid);

		DecentPlayer player;
		if ((player = playerMap.remove(uuid)) != null) {
			player.destroy();
		}
		return player;
	}

	/**
	 * Get the UUIDs of all registered DecentPlayers.
	 * @return Set of the UUIDs of all registered DecentPlayers.
	 */
	public Set<UUID> getPlayerUuids() {
		return playerMap.keySet();
	}

	/**
	 * Get all registered DecentPlayers.
	 * @return Set of all registered DecentPlayers.
	 */
	public Collection<DecentPlayer> getPlayers() {
		return playerMap.values();
	}

}
