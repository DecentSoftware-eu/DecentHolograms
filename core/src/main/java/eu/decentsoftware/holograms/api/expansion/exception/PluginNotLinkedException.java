package eu.decentsoftware.holograms.api.expansion.exception;

import eu.decentsoftware.holograms.api.expansion.Expansion;

public class PluginNotLinkedException extends RuntimeException {

    public PluginNotLinkedException(Expansion expansion) {
        super("Expansion " + expansion.getName() + " is not linked to any plugin.");
    }
}
