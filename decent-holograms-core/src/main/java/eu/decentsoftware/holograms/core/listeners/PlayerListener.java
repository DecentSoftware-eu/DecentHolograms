package eu.decentsoftware.holograms.core.listeners;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {

	private static final DecentHolograms PLUGIN = DecentHologramsProvider.getDecentHolograms();

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player player = e.getPlayer();
		PLUGIN.getPlayerManager().createPlayer(player);
		Bukkit.getScheduler().runTaskAsynchronously(PLUGIN.getPlugin(), () ->
				PLUGIN.getHologramManager().showAll(player)
		);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player player = e.getPlayer();
		PLUGIN.getPlayerManager().removePlayer(player.getUniqueId());
		Bukkit.getScheduler().runTaskAsynchronously(PLUGIN.getPlugin(), () ->
				PLUGIN.getHologramManager().hideAll(player)
		);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Player player = e.getPlayer();
		Bukkit.getScheduler().runTaskAsynchronously(PLUGIN.getPlugin(), () ->
				PLUGIN.getHologramManager().hideAll(player)
		);
	}

	@EventHandler
	public void onChangeWorld(PlayerChangedWorldEvent e) {
		Player player = e.getPlayer();
		Bukkit.getScheduler().runTaskAsynchronously(PLUGIN.getPlugin(), () ->
				PLUGIN.getHologramManager().hideAll(player)
		);
	}

}
