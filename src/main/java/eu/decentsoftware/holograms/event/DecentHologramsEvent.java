package eu.decentsoftware.holograms.event;

import org.bukkit.event.Event;

/**
 * This is the base event for all DecentHolograms events.
 *
 * @author d0by
 * @since 2.7.8
 */
public abstract class DecentHologramsEvent extends Event {

    protected DecentHologramsEvent() {
        super();
    }

    protected DecentHologramsEvent(boolean isAsync) {
        super(isAsync);
    }

}
