package eu.decentsoftware.holograms.api.utils.event;

import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import eu.decentsoftware.holograms.event.DecentHologramsReloadEvent;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@UtilityClass
public class EventFactory {

    public static boolean handleHologramInteractEvent(Player player, Hologram hologram, HologramPage page, ClickType clickType, int entityId) {
        if (HologramClickEvent.getHandlerList().getRegisteredListeners().length == 0) return true;

        HologramClickEvent event = new HologramClickEvent(player, hologram, page, clickType, entityId);
        Bukkit.getPluginManager().callEvent(event);

        return !event.isCancelled();
    }

    public static boolean handleReloadEvent() {
        if (DecentHologramsReloadEvent.getHandlerList().getRegisteredListeners().length == 0) return true;

        DecentHologramsReloadEvent event = new DecentHologramsReloadEvent();
        Bukkit.getPluginManager().callEvent(event);

        return true; // Not cancellable
    }

}
