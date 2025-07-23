package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.nms.api.NmsAdapter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This service is responsible for managing packet listener for online players.
 *
 * @author d0by
 * @since 2.9.0
 */
public class NmsPacketListenerService {

    private final NmsAdapter nmsAdapter;
    private final NmsPlayerListener playerListener;

    public NmsPacketListenerService(JavaPlugin plugin, NmsAdapter nmsAdapter) {
        this.nmsAdapter = nmsAdapter;
        this.playerListener = new NmsPlayerListener(this);

        Bukkit.getPluginManager().registerEvents(playerListener, plugin);
        registerListenerForAllPlayers();
    }

    /**
     * Shutdown this service.
     * This method should be called when the plugin is being disabled.
     */
    public void shutdown() {
        HandlerList.unregisterAll(playerListener);
        unregisterListenerForAllPlayers();
    }

    /**
     * Register a packet listener for the given player.
     *
     * @param player The player.
     */
    void registerListener(Player player) {
        nmsAdapter.registerPacketListener(player);
    }

    /**
     * Unregister a packet listener for the given player.
     *
     * @param player The player.
     */
    void unregisterListener(Player player) {
        nmsAdapter.unregisterPacketListener(player);
    }

    private void registerListenerForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::registerListener);
    }

    private void unregisterListenerForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::unregisterListener);
    }

}
