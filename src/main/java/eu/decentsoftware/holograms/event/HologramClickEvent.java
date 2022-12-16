package eu.decentsoftware.holograms.event;

import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when a player clicks on a Hologram.
 */
@Getter
public class HologramClickEvent extends DecentHologramsEvent implements Cancellable {

    private boolean cancelled = false;
    private final @NotNull Player player;
    private final @NotNull Hologram hologram;
    private final @NotNull HologramPage page;
    private final @NotNull ClickType click;
    private final int entityId;

    public HologramClickEvent(@NotNull Player player, @NotNull Hologram hologram, @NotNull HologramPage page, @NotNull ClickType click, int entityId) {
        super(true);
        this.player = player;
        this.hologram = hologram;
        this.page = page;
        this.click = click;
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

}
