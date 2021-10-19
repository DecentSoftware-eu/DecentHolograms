package eu.decentsoftware.holograms.core.player;

import eu.decentsoftware.holograms.api.player.DecentPlayer;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DecentPlayerImpl implements DecentPlayer {

	private final UUID uuid;
	private final String name;

	/*
	 *	Constructors
	 */

	public DecentPlayerImpl(@NonNull final Player player) {
		this.uuid = player.getUniqueId();
		this.name = player.getName();
	}

	/*
	 *	General Methods
	 */

	@Override
	public void destroy() {}

	@Override
	public UUID getUniqueId() {
		return uuid;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}

}
