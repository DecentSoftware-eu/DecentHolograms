package eu.decentsoftware.holograms.api.expansion.listener;

import eu.decentsoftware.holograms.api.expansion.ExpansionRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

public class ExpansionWatchingListener implements Listener {
    private final ExpansionRegistry registry;

    public ExpansionWatchingListener(ExpansionRegistry registry) {
        this.registry = registry;
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        registry.unregisterExpansionsForPlugin(event.getPlugin());
    }
}
