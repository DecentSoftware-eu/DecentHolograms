package eu.decentsoftware.holograms.api.expansion;

import eu.decentsoftware.holograms.api.expansion.source.ExpansionSource;
import eu.decentsoftware.holograms.api.utils.Log;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
        if (expansionIdsBySource.containsKey(source.getId())) {
            throw new IllegalStateException("Expansion source '" + source.getId() + "' is already registered.");
        }

        sources.put(source.getId(), source);
        Log.info("Registered expansion source: " + source.getId() + ", loading expansions...");

        Iterable<? extends Expansion> expansions = source.loadExpansions();
        expansions.forEach(registry::registerExpansion);
        expansionIdsBySource.put(
                source.getId(),
                StreamSupport.stream(expansions.spliterator(), false)
                        .map(Expansion::getId)
                        .collect(Collectors.toList()));
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
