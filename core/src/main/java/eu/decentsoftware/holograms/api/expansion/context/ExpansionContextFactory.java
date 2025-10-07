package eu.decentsoftware.holograms.api.expansion.context;

import eu.decentsoftware.holograms.api.expansion.Expansion;

public interface ExpansionContextFactory {

    /**
     * Creates an ExpansionContext for the given Expansion.
     *
     * @param expansion the expansion to create the context for
     * @return the created ExpansionContext
     */
    ExpansionContext createExpansionContext(Expansion expansion);
}
