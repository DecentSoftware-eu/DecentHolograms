package eu.decentsoftware.holograms.api.player;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerListener implements Listener {

    private final DecentHolograms decentHolograms;

    public PlayerListener(DecentHolograms decentHolograms) {
        this.decentHolograms = decentHolograms;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        decentHolograms.getPlayerManager().createPlayer(player);
        decentHolograms.getPacketListener().register(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        decentHolograms.getPlayerManager().removePlayer(player.getUniqueId());
        decentHolograms.getHologramManager().onQuit(player);
        decentHolograms.getPacketListener().unregister(player);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().runTaskAsynchronously(decentHolograms.getPlugin(), () ->
                Hologram.getCachedHolograms().forEach(hologram -> hologram.hide(player))
        );
    }

}
