package eu.decentsoftware.holograms.nms;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

class NmsPlayerListener implements Listener {

    private final NmsPacketListenerService service;

    NmsPlayerListener(NmsPacketListenerService service) {
        this.service = service;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        service.registerListener(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        service.unregisterListener(event.getPlayer());
    }

}
