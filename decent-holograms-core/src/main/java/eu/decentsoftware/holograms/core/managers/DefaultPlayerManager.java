package eu.decentsoftware.holograms.core.managers;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.api.managers.PlayerManager;
import eu.decentsoftware.holograms.api.player.DecentPlayer;
import eu.decentsoftware.holograms.core.player.DecentPlayerImpl;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class DefaultPlayerManager implements PlayerManager {

	private final Map<UUID, DecentPlayer> playerMap = Maps.newHashMap();

	public DefaultPlayerManager() {
		this.reload();
	}

	@Override
	public void reload() {
		Bukkit.getOnlinePlayers().forEach((player) -> {
			if (!this.containsPlayer(player.getUniqueId())) {
				this.createPlayer(player);
			}
		});
	}

	@Override
	public void destroy() {
		for (DecentPlayer decentPlayer : playerMap.values()) {
			decentPlayer.destroy();
		}
		playerMap.clear();
	}

	@Override
	public boolean containsPlayer(@NonNull final UUID uuid) {
		return playerMap.containsKey(uuid);
	}

	@Override
	public DecentPlayer createPlayer(@NonNull final Player player) {
		return playerMap.put(player.getUniqueId(), new DecentPlayerImpl(player));
	}

	@Override
	public DecentPlayer getPlayer(@NonNull final UUID uuid) {
		return playerMap.get(uuid);
	}

	@Override
	public DecentPlayer removePlayer(@NonNull final UUID uuid) {
		DecentPlayer player;
		if ((player = playerMap.remove(uuid)) != null) {
			player.destroy();
		}
		return player;
	}

	@Override
	public Set<UUID> getPlayerUuids() {
		return playerMap.keySet();
	}

	@Override
	public Collection<DecentPlayer> getPlayers() {
		return playerMap.values();
	}

}
