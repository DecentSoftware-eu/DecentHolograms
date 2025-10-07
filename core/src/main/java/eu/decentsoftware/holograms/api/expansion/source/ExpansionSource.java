package eu.decentsoftware.holograms.api.expansion.source;

import eu.decentsoftware.holograms.api.expansion.Expansion;

public interface ExpansionSource {

    /**
     * Gets the unique identifier of this expansion source.
     *
     * @return The unique identifier.
     */
    String getId();

    /**
     * Loads all available expansions from this source.
     *
     * @return An iterable collection of expansions.
     */
    Iterable<? extends Expansion> loadExpansions();
}
