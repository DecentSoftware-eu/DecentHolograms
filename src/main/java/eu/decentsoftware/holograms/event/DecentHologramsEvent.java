package eu.decentsoftware.holograms.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This is the base event for all DecentHolograms events.
 *
 * @author d0by
 * @since 2.7.8
 */
public abstract class DecentHologramsEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public DecentHologramsEvent() {
        super();
    }

    public DecentHologramsEvent(boolean isAsync) {
        super(isAsync);
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
