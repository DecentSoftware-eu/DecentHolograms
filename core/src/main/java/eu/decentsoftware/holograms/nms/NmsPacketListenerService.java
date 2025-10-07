package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.nms.api.NmsAdapter;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;
import eu.decentsoftware.holograms.nms.api.NmsPacketListenerDelegate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * This service is responsible for managing packet listener for online players.
 *
 * @author d0by
 * @since 2.9.0
 */
public class NmsPacketListenerService {

    private final NmsAdapter nmsAdapter;
    private final NmsPacketListener packetListener;
    private final NmsPlayerListener playerListener;

    private final Map<String, NmsPacketListenerDelegate> delegates;
    private final List<NmsPacketListener> autoListeners;

    public NmsPacketListenerService(JavaPlugin plugin, NmsAdapter nmsAdapter, NmsPacketListener packetListener) {
        this.nmsAdapter = nmsAdapter;
        this.packetListener = packetListener;
        this.playerListener = new NmsPlayerListener(this);
        this.delegates = new ConcurrentHashMap<>();
        this.autoListeners = new CopyOnWriteArrayList<>();

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
        NmsPacketListenerDelegate delegate = new NmsPacketListenerDelegate(Collections.singletonList(packetListener));
        for (NmsPacketListener autoListener : autoListeners) {
            delegate.addListener(autoListener);
        }

        delegates.put(player.getName(), delegate);

        nmsAdapter.registerPacketListener(player, packetListener);
    }

    /**
     * Unregister a packet listener for the given player.
     *
     * @param player The player.
     */
    void unregisterListener(Player player) {
        delegates.remove(player.getName());

        nmsAdapter.unregisterPacketListener(player);
    }

    /**
     * Register an automatic packet listener.
     * The auto listeners will be added to every player that joins the server.
     *
     * @param listener The listener to add.
     */
    public void registerAutoListener(NmsPacketListener listener) {
        autoListeners.add(listener);

        delegates.values().forEach(delegate -> delegate.addListener(listener));
    }

    /**
     * Unregister an automatic packet listener.
     *
     * @param listener The listener to remove.
     */
    public void unregisterAutoListener(NmsPacketListener listener) {
        autoListeners.remove(listener);

        delegates.values().forEach(delegate -> delegate.removeListener(listener));
    }

    /**
     * Add a packet listener for the given player.
     *
     * @param player The player.
     * @param listener The listener to add.
     */
    public void addPacketListener(Player player, NmsPacketListener listener) {
        NmsPacketListenerDelegate delegate = delegates.get(player.getName());

        if (delegate != null) {
            delegate.addListener(listener);
        }
    }

    /**
     * Remove a packet listener for the given player.
     *
     * @param player The player.
     * @param listener The listener to remove.
     */
    public void removePacketListener(Player player, NmsPacketListener listener) {
        NmsPacketListenerDelegate delegate = delegates.get(player.getName());

        if (delegate != null) {
            delegate.removeListener(listener);
        }
    }

    private void registerListenerForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::registerListener);
    }

    private void unregisterListenerForAllPlayers() {
        Bukkit.getOnlinePlayers().forEach(this::unregisterListener);
    }

}
