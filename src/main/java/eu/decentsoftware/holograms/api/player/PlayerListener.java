package eu.decentsoftware.holograms.api.player;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.holograms.Hologram;
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
        decentHolograms.getPacketListener().register(player);
        if (player.hasPermission("dh.admin") && decentHolograms.isUpdateAvailable()) {
            Lang.sendUpdateMessage(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        decentHolograms.getHologramManager().onQuit(player);
        decentHolograms.getPacketListener().unregister(player);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Hologram.getCachedHolograms().forEach(hologram -> hologram.hide(player));
    }

//    @EventHandler
//    public void onTeleport(PlayerChangedWorldEvent e) {
//        Player player = e.getPlayer();
//        Hologram.getCachedHolograms().forEach(hologram -> hologram.hide(player));
//    }

}
