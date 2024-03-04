package eu.decentsoftware.holograms.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This event is called after the DecentHolograms plugin is reloaded.
 *
 * @author d0by
 * @since 2.7.8
 */
public class DecentHologramsRegisterEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public DecentHologramsRegisterEvent() {
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public static boolean isRegistered() {
        return HANDLERS.getRegisteredListeners().length > 0;
    }

}
