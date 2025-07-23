package eu.decentsoftware.holograms.nms.api.event;

import org.bukkit.entity.Player;

/**
 * An event that is fired when a player interacts with an entity.
 *
 * <p>This is not a Bukkit event, and cannot be caught with a Bukkit listener!</p>
 *
 * @author d0by
 * @since 2.9.0
 */
public class NmsEntityInteractEvent {

    private final Player player;
    private final int entityId;

    public NmsEntityInteractEvent(Player player, int entityId) {
        this.player = player;
        this.entityId = entityId;
    }

    /**
     * Get the player that interacted with the entity.
     *
     * @return The player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the ID of the entity the player interacted with.
     *
     * @return The entity ID.
     */
    public int getEntityId() {
        return entityId;
    }

}
