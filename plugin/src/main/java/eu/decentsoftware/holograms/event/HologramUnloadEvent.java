package eu.decentsoftware.holograms.event;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.Getter;
import org.bukkit.event.HandlerList;

/**
 * This event is called whenever a Hologram is unloaded via
 * {@link Hologram#disable(eu.decentsoftware.holograms.api.holograms.DisableCause)}
 */
@Getter
public class HologramUnloadEvent extends DecentHologramsEvent{

    private static final HandlerList HANDLERS = new HandlerList();
    private final Hologram hologram;

    public HologramUnloadEvent(boolean isAsync, Hologram hologram) {
        super(isAsync);
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
