package eu.decentsoftware.holograms.event;

import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
public class HologramClickEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private boolean cancel = false;
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
        return this.cancel;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
