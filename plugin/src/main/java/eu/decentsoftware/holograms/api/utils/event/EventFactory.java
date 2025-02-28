package eu.decentsoftware.holograms.api.utils.event;

import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import eu.decentsoftware.holograms.event.DecentHologramsReloadEvent;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Utility class for firing custom events.
 */
@UtilityClass
public class EventFactory {

    /**
     * Fire a hologram click event.
     *
     * @param player    The player that clicked.
     * @param hologram  The hologram that was clicked.
     * @param page      The page that was clicked.
     * @param clickType The type of click.
     * @param entityId  The entity id of the hologram.
     * @return Whether the click should be processed. (The event was NOT canceled)
     * @see HologramClickEvent
     */
    public static boolean fireHologramClickEvent(Player player, Hologram hologram, HologramPage page, ClickType clickType, int entityId) {
        if (HologramClickEvent.getHandlerList().getRegisteredListeners().length == 0) {
            return true;
        }

        HologramClickEvent event = new HologramClickEvent(player, hologram, page, clickType, entityId);
        Bukkit.getPluginManager().callEvent(event);

        return !event.isCancelled();
    }

    /**
     * Fire a plugin reload event.
     *
     * @see DecentHologramsReloadEvent
     */
    public static void fireReloadEvent() {
        if (DecentHologramsReloadEvent.getHandlerList().getRegisteredListeners().length == 0) {
            return;
        }

        DecentHologramsReloadEvent event = new DecentHologramsReloadEvent();
        Bukkit.getPluginManager().callEvent(event);
    }

}
