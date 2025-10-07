package eu.decentsoftware.holograms.api.expansion;

import eu.decentsoftware.holograms.api.expansion.source.ExpansionSource;
import eu.decentsoftware.holograms.api.utils.Log;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultExpansionSourceRegistry implements ExpansionSourceRegistry {
    private final ExpansionRegistry registry;

    private final Map<String, ExpansionSource> sources;
    private final Map<String, List<String>> expansionIdsBySource;

    public DefaultExpansionSourceRegistry(ExpansionRegistry registry) {
        this.registry = registry;
        this.sources = new ConcurrentHashMap<>();
        this.expansionIdsBySource = new ConcurrentHashMap<>();
    }

    @Override
    public void registerSource(ExpansionSource source) {
        List<String> ids = new ArrayList<>();
        if (expansionIdsBySource.put(source.getId(), ids) != null) {
            throw new IllegalStateException("Expansion source '" + source.getId() + "' is already registered.");
        }

        Log.info("Registered expansion source: " + source.getId() + ", loading expansions...");

        source.loadExpansions().forEach(expansion -> {
            ids.add(expansion.getId());

            registry.registerExpansion(expansion);
        });

        sources.put(source.getId(), source);
    }

    @Override
    public void unregisterSource(ExpansionSource source) {
        List<String> ids = expansionIdsBySource.remove(source.getId());
        if (ids == null) {
            throw new IllegalStateException("Expansion source '" + source.getId() + "' is not registered.");
        }

        ids.forEach(id -> {
            Expansion expansion = registry.getExpansion(id);
            if (expansion == null) {
                return;
            }

            registry.unregisterExpansion(id);
        });

        sources.remove(source.getId());
    }

    @Override
    public Collection<ExpansionSource> getAllSources() {
        return Collections.unmodifiableCollection(sources.values());
    }
}
