package eu.decentsoftware.holograms.api.context;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.reflect.Version;

public class DefaultAppContextFactory implements AppContextFactory {

    @Override
    public AppContext createAppContext() {
        return new DefaultAppContext(Version.CURRENT, DecentHologramsAPI.get().getPlugin());
    }
}
