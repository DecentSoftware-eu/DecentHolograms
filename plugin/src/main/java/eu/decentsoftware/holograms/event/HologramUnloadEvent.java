package eu.decentsoftware.holograms.event;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.event.HandlerList;

/**
 * This event is called whenever a Hologram is unloaded via
 * {@link Hologram#disable(eu.decentsoftware.holograms.api.holograms.DisableCause)}
 */
public class HologramUnloadEvent extends DecentHologramsEvent{

    private static final HandlerList HANDLERS = new HandlerList();
    private final Hologram hologram;

    public HologramUnloadEvent(Hologram hologram) {
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
     * The {@link Hologram} that gets unloaded.
     * @return The Hologram that gets unloaded.
     */
    public Hologram getHologram() {
        return hologram;
    }
}
