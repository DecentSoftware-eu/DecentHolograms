package eu.decentsoftware.holograms.api.expansion.event;

import eu.decentsoftware.holograms.api.expansion.Expansion;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;

public class ExpansionRegisteredEvent extends Event {
    private static final HandlerList handlerList = new HandlerList();

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    private final Expansion expansion;
    private final Plugin plugin;

    public ExpansionRegisteredEvent(Expansion expansion, Plugin plugin) {
        this.expansion = expansion;
        this.plugin = plugin;
    }

    public Expansion getExpansion() {
        return expansion;
    }

    /**
     * Get the plugin that holds the expansion
     *
     * @return the plugin
     */
    public Plugin getHolderPlugin() {
        return plugin;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
