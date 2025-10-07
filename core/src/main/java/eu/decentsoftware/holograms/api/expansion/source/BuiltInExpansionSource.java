package eu.decentsoftware.holograms.api.expansion.source;

import eu.decentsoftware.holograms.api.expansion.Expansion;
import eu.decentsoftware.holograms.api.expansion.ExpansionLoader;
import org.bukkit.plugin.Plugin;

public class BuiltInExpansionSource implements ExpansionSource {
    private final ExpansionLoader loader;
    private final Plugin plugin;

    public BuiltInExpansionSource(ExpansionLoader loader, Plugin plugin) {
        this.loader = loader;
        this.plugin = plugin;
    }

    @Override
    public String getId() {
        return "built-in";
    }

    @Override
    public Iterable<? extends Expansion> loadExpansions() {
        return loader.loadExpansions(plugin.getClass().getClassLoader());
    }
}
