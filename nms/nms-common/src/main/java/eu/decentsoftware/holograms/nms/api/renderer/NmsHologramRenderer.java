package eu.decentsoftware.holograms.nms.api.renderer;

import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.Player;

/**
 * Represents a renderer for holographic elements in the game.
 *
 * <p>Implementations of this interface handle rendering specific types of hologram components,
 * such as text, entities, or items, making them visible to players.</p>
 *
 * @param <T> The type of content that this renderer handles (e.g., text, entity type, or item).
 * @author d0by
 * @since 2.9.0
 */
public interface NmsHologramRenderer<T> {

    /**
     * Displays the hologram content to the specified player at the given position.
     *
     * @param player   The player who will see the hologram.
     * @param position The position where the hologram should be displayed.
     * @param content  The content to render (e.g., text, entity, or item).
     */
    void display(Player player, DecentPosition position, T content);

    /**
     * Updates the content of an already displayed hologram for the given player.
     * This can be used to modify text, change an item, or update an entity.
     *
     * @param player   The player who sees the hologram.
     * @param position The position of the existing hologram.
     * @param content  The new content to update the hologram with.
     */
    void updateContent(Player player, DecentPosition position, T content);

    /**
     * Moves the hologram to a new position for the specified player.
     *
     * @param player   The player who sees the hologram.
     * @param position The new position where the hologram should be moved.
     */
    void move(Player player, DecentPosition position);

    /**
     * Hides the hologram from the specified player.
     *
     * @param player The player from whom the hologram should be hidden.
     */
    void hide(Player player);

    /**
     * Gets the height of the rendered content.
     *
     * <p>This is used to determine spacing and positioning relative to other hologram elements.</p>
     *
     * @param content The content whose height should be retrieved.
     * @return The height of the content.
     */
    double getHeight(T content);

    /**
     * Get the entity ids used by this renderer.
     *
     * @return The entity ids.
     */
    int[] getEntityIds();

}