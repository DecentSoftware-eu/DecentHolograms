package eu.decentsoftware.holograms.api.expansion.config;

import eu.decentsoftware.holograms.api.expansion.Expansion;

public interface ExpansionConfigSource {

    /**
     * Load the config of the expansion, if it does not exist create a new one with default values
     *
     * @param expansion the expansion
     * @return the config of the expansion
     */
    ExpansionConfig loadOrCreateConfig(Expansion expansion);

    /**
     * Save the config of the expansion
     *
     * @param expansion the expansion
     * @param config the config to save
     */
    void saveConfig(Expansion expansion, ExpansionConfig config);
}
