package eu.decentsoftware.holograms.event;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * Abstract event class for temporary hologram spawn events. This class is extended by specific events such
 * as TemporaryDamageHologramSpawnEvent and TemporaryHealHologramSpawnEvent.<br/>
 *
 * @author jamailun
 */
@Getter
public abstract class TemporaryHologramSpawnEvent extends DecentHologramsEvent implements Cancellable {

    private boolean cancelled = false;
    private final @NotNull Entity entity;
    private @NotNull String text;
    private @NotNull Location location;

    /**
     * Constructor for the TemporaryHologramSpawnEvent.
     * @param entity The entity associated with the event.
     * @param text The text of the hologram that will be spawned.
     * @param location The location of the hologram that will be spawned.
     */
    protected TemporaryHologramSpawnEvent(@NotNull Entity entity, @NotNull String text, @NotNull Location location) {
        super(false);
        this.entity = entity;
        this.text = text;
        this.location = location;
    }

    /**
     * Sets the text of the hologram that will be spawned.
     * @param text The text to set for the hologram.
     */
    public void setText(@NotNull String text) {
        this.text = text;
    }

    /**
     * Sets the location of the hologram that will be spawned.
     * @param location The location to set for the hologram.
     */
    public void setLocation(@NotNull Location location) {
        this.location = location;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

}
