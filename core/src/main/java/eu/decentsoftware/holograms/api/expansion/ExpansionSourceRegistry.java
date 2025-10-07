package eu.decentsoftware.holograms.api.expansion;

import eu.decentsoftware.holograms.api.expansion.source.ExpansionSource;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

public interface ExpansionSourceRegistry {

    /**
     * Adds a source to the expansion service.
     * This hereby loads and activates all expansions provided by the source.
     *
     * @param source the source to add
     */
    void registerSource(ExpansionSource source);

    /**
     * Removes a source from the expansion service.
     * This hereby deactivates and unloads all expansions provided by the source.
     *
     * @param source the source to remove
     */
    void unregisterSource(ExpansionSource source);

    /**
     * Gets all registered sources.
     *
     * @return a collection of all registered sources
     */
    @Unmodifiable
    Collection<ExpansionSource> getAllSources();
}
