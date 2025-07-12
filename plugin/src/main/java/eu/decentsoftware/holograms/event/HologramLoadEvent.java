package eu.decentsoftware.holograms.event;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.event.HandlerList;

/**
 * This event is called whenever a Hologram is loaded via
 * {@link Hologram#enable()}
 */
public class HologramLoadEvent extends DecentHologramsEvent{

    private static final HandlerList HANDLERS = new HandlerList();

    private final Hologram hologram;

    public HologramLoadEvent(Hologram hologram){
        this.hologram = hologram;
    }

    @Override
    public HandlerList getHandlers() {
        return null;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    /**
     * The {@link Hologram} that gets loaded.
     * @return The Hologram that gets loaded.
     */
    public Hologram getHologram() {
        return hologram;
    }
}
