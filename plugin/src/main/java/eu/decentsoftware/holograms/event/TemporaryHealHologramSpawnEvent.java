package eu.decentsoftware.holograms.event;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a temporary hologram is spawned, from the "healing display" feature.<br/>
 * Cancelling the event will prevent the hologram from being spawned.
 */
@Getter
public class TemporaryHealHologramSpawnEvent extends TemporaryHologramSpawnEvent {

    private static final HandlerList HANDLERS = new HandlerList();

    @Getter private final double amount;

  /**
   * New event instance.
   * @param livingEntity living entity that has been healed.
   * @param text text of the hologram that will be spawned.
   * @param amount amount of healing that has been done.
   * @param location location of the hologram that will be spawned.
   */
    public TemporaryHealHologramSpawnEvent(@NotNull LivingEntity livingEntity, @NotNull String text, double amount, @NotNull Location location) {
        super(livingEntity, text, location);
        this.amount = amount;
    }

    @Override
    public HandlerList getHandlers() {
      return HANDLERS;
    }

    public static HandlerList getHandlerList() {
      return HANDLERS;
    }

}
