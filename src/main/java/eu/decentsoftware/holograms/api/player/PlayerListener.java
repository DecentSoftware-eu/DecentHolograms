package eu.decentsoftware.holograms.api.player;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerListener implements Listener {

    private static final DecentHolograms DH = DecentHologramsAPI.get();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        S.async(() -> DH.getHologramManager().updateVisibility(player));
        S.sync(() -> DH.getPacketListener().hook(player));
        if (player.hasPermission("dh.admin") && DH.isUpdateAvailable()) {
            Lang.sendUpdateMessage(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        S.async(() -> DH.getHologramManager().onQuit(player));
        DH.getPacketListener().unhook(player);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        S.async(() -> DH.getHologramManager().updateVisibility(player));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        S.async(() -> DH.getHologramManager().updateVisibility(player));
    }

}
