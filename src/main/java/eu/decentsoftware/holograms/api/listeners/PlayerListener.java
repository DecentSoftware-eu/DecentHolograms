package eu.decentsoftware.holograms.api.listeners;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

@SuppressWarnings("unused")
public class PlayerListener implements Listener {

    private final DecentHolograms decentHolograms;

    public PlayerListener(DecentHolograms decentHolograms) {
        this.decentHolograms = decentHolograms;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        S.async(() -> decentHolograms.getHologramManager().updateVisibility(player));
        decentHolograms.getPacketListener().hook(player);
        if (decentHolograms.isUpdateAvailable() && player.hasPermission("dh.admin")) {
            Lang.sendUpdateMessage(player);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        S.async(() -> decentHolograms.getHologramManager().onQuit(player));
        decentHolograms.getPacketListener().unhook(player);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        S.async(() -> decentHolograms.getHologramManager().hideAll(player));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        S.async(() -> decentHolograms.getHologramManager().hideAll(player));
    }

}
