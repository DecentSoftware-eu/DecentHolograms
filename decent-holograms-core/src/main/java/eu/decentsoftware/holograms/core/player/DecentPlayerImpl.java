package eu.decentsoftware.holograms.core.player;

import eu.decentsoftware.holograms.api.DecentHologramsProvider;
import eu.decentsoftware.holograms.api.player.DecentPlayer;
import eu.decentsoftware.holograms.utils.scheduler.ConsumerTask;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DecentPlayerImpl implements DecentPlayer {

	private final UUID uuid;
	private final String name;
	private final ConsumerTask<DecentPlayer> updateTask;

	/*
	 *	Constructors
	 */

	public DecentPlayerImpl(@NonNull final Player player) {
		this.uuid = player.getUniqueId();
		this.name = player.getName();

		this.updateTask = new ConsumerTask<>(DecentHologramsProvider.getDecentHolograms().getPlugin(), this);
		this.updateTask.start();
	}

	/*
	 *	General Methods
	 */

	@Override
	public void destroy() {
		this.updateTask.stop();
	}

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

	@Override
	public ConsumerTask<DecentPlayer> getUpdateTask() {
		return updateTask;
	}

}
