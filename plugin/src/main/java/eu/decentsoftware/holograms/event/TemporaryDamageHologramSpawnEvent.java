package eu.decentsoftware.holograms.event;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This event is called when a temporary hologram is spawned, from the "damage display" feature.<br/>
 * Cancelling the event will prevent the hologram from being spawned.
 */
@Getter
public class TemporaryDamageHologramSpawnEvent extends TemporaryHologramSpawnEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter private final double amount;
    @Getter private final @Nullable Entity damager;

    /**
     * New event instance.
     * @param entity entity that has been damaged.
     * @param text text of the hologram that will be spawned.
     * @param location location of the hologram that will be spawned.
     * @param amount amount of damage that has been done.
     * @param damager the entity that caused the damage, can be null if the damage was not caused by an entity.
     */
    public TemporaryDamageHologramSpawnEvent(@NotNull Entity entity, @NotNull String text, @NotNull Location location, double amount, @Nullable Entity damager) {
        super(entity, text, location);
        this.amount = amount;
        this.damager = damager;
    }

    @Override
    public HandlerList getHandlers() {
      return HANDLERS;
    }

    public static HandlerList getHandlerList() {
      return HANDLERS;
    }

}
