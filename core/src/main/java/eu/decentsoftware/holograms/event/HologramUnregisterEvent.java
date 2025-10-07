package eu.decentsoftware.holograms.event;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.Getter;
import org.bukkit.event.HandlerList;

/**
 * This event is called whenever a Hologram is unregistered through
 * {@link eu.decentsoftware.holograms.api.holograms.HologramManager#destroy()}
 */
@Getter
public class HologramUnregisterEvent extends DecentHologramsEvent {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Hologram hologram;

    public HologramUnregisterEvent(boolean isAsync, Hologram hologram) {
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
