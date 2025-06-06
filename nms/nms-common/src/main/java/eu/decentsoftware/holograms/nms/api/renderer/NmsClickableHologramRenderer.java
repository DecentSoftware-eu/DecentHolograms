package eu.decentsoftware.holograms.nms.api.renderer;

import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.Player;

/**
 * A renderer for clickable entities, used for click detections in holograms.
 *
 * @author d0by
 * @since 2.9.0
 */
public interface NmsClickableHologramRenderer {

    /**
     * Spawn the clickable entity.
     *
     * @param player   The player to spawn the entity for.
     * @param position The position to spawn the entity at.
     */
    void display(Player player, DecentPosition position);

    /**
     * Move the clickable entity.
     *
     * @param player   The player to move the entity for.
     * @param position The position to move the entity to.
     */
    void move(Player player, DecentPosition position);

    /**
     * Hide the clickable entity.
     *
     * @param player The player to hide the entity for.
     */
    void hide(Player player);

    /**
     * Get the ID of the clickable entity.
     *
     * @return The ID of the clickable entity.
     */
    int getEntityId();

}
