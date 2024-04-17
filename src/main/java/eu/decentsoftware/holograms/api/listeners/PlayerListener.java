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

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class PlayerListener implements Listener {

    private final DecentHolograms decentHolograms;
    private final Map<UUID, Long> playerLoginTime = new ConcurrentHashMap<>(); // Hologram lines can be updated asynchronously

    public PlayerListener(DecentHolograms decentHolograms) {
        this.decentHolograms = decentHolograms;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        decentHolograms.getPacketListener().hook(player);
        if (decentHolograms.isUpdateAvailable() && player.hasPermission("dh.admin")) {
            Lang.sendUpdateMessage(player);
        }

        playerLoginTime.put(player.getUniqueId(), System.currentTimeMillis());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        S.async(() -> decentHolograms.getHologramManager().onQuit(player));
        decentHolograms.getPacketListener().unhook(player);
        playerLoginTime.remove(player.getUniqueId());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        // TODO: All holograms (and entities) get hidden on the client, when the client
        //  teleports or respawns. This only seems to be happening on some client versions
        //  so we need to find which versions are affected and only re-show the holograms
        //  to those clients (or on those server versions).
        //  -
        //  For now, this only causes visual glitches where even if a player gets teleported
        //  by a fraction of a block, the holograms still disappear and reappear for them.
        //  -
        //  tl:dr Figure out which versions need this.
        S.async(() -> decentHolograms.getHologramManager().hideAll(player));
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player player = e.getPlayer();
        S.async(() -> decentHolograms.getHologramManager().hideAll(player));
    }

    public int getTicksSinceLogin(Player player) {
        Long epoch = playerLoginTime.get(player.getUniqueId());

        if (epoch == null) {
            return 0;
        }

        return (int) ((System.currentTimeMillis() - epoch)) / 50;
    }
}
