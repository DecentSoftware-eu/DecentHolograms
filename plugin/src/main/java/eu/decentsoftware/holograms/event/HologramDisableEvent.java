package eu.decentsoftware.holograms.event;

import eu.decentsoftware.holograms.api.holograms.DisableCause;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.Getter;
import org.bukkit.event.HandlerList;

/**
 * This event is called whenever a Hologram gets disabled through
 * {@link Hologram#disable(DisableCause)}
 */
@Getter
public class HologramDisableEvent extends DecentHologramsEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Hologram hologram;

    public HologramDisableEvent(boolean async, Hologram hologram) {
        super(async);
        this.hologram = hologram;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
