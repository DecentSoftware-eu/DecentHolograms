package eu.decentsoftware.holograms.nms.api;

import eu.decentsoftware.holograms.nms.api.renderer.NmsHologramRendererFactory;
import org.bukkit.entity.Player;

/**
 * This class serves as the main access-point to an NMS implementation.
 *
 * @author d0by
 * @since 2.9.0
 */
public interface NmsAdapter {

    /**
     * Get the factory for creating hologram renderers.
     *
     * @return An instance of {@link NmsHologramRendererFactory}.
     */
    NmsHologramRendererFactory getHologramComponentFactory();

    /**
     * Register a packet listener for a player.
     *
     * <p>A player can only have a single listener registered.</p>
     *
     * @param player   The player to register the listener for.
     * @param listener The listener to register.
     */
    void registerPacketListener(Player player, NmsPacketListener listener);

    /**
     * Unregister a packet listener for a player.
     *
     * <p>If there is not registered listener for the given player, nothing will happen.</p>
     *
     * @param player The player to unregister the listener for.
     */
    void unregisterPacketListener(Player player);

}
