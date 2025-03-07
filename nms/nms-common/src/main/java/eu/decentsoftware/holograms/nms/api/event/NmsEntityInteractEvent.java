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
    private final NmsEntityInteractAction action;
    private boolean handled = false;

    public NmsEntityInteractEvent(Player player, int entityId, NmsEntityInteractAction action) {
        this.player = player;
        this.entityId = entityId;
        this.action = action;
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

    /**
     * Get the type of interaction between the player and the entity.
     *
     * @return The interaction action.
     */
    public NmsEntityInteractAction getAction() {
        return action;
    }

    /**
     * Check if the event is considered handled, meaning the click will be canceled,
     * and the server will not be able to detect it.
     *
     * @return True if the event is considered handled, false otherwise.
     */
    public boolean isHandled() {
        return handled;
    }

    /**
     * Set if the event should be considered handled, meaning the click will be canceled,
     * and the server will not be able to detect it.
     *
     * @param handled True if the event should be considered handled, false otherwise.
     */
    public void setHandled(boolean handled) {
        this.handled = handled;
    }

}
