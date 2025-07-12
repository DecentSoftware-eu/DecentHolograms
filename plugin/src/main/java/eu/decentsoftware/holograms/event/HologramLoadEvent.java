package eu.decentsoftware.holograms.event;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.Getter;
import org.bukkit.event.HandlerList;

/**
 * This event is fired whenever a Hologram is loaded.
 */
@Getter
public class HologramLoadEvent extends DecentHologramsEvent{
    
    private static final HandlerList HANDLERS = new HandlerList();
    
    private final Hologram hologram;
    
    public HologramLoadEvent(boolean async, Hologram hologram) {
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
