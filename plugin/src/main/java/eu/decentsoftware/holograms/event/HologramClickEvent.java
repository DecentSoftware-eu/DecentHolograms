package eu.decentsoftware.holograms.event;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a player clicks on a Hologram.
 */
@Getter
public class HologramClickEvent extends DecentHologramsEvent implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    private boolean cancelled = false;
    private final @NotNull Player player;
    private final @NotNull Hologram hologram;
    private final @NotNull HologramPage page;
    private final int entityId;

    public HologramClickEvent(@NotNull Player player, @NotNull Hologram hologram, @NotNull HologramPage page,  int entityId) {
        super(true);
        this.player = player;
        this.hologram = hologram;
        this.page = page;
        this.entityId = entityId;
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public static boolean isRegistered() {
        return HANDLERS.getRegisteredListeners().length > 0;
    }

}
