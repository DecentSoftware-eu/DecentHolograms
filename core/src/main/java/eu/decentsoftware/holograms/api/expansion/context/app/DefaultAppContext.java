package eu.decentsoftware.holograms.api.expansion.context.app;

import eu.decentsoftware.holograms.api.utils.reflect.Version;
import org.bukkit.plugin.java.JavaPlugin;

public class DefaultAppContext implements AppContext {
    private final Version serverVersion;
    private final JavaPlugin plugin;

    public DefaultAppContext(Version serverVersion, JavaPlugin plugin) {
        this.serverVersion = serverVersion;
        this.plugin = plugin;
    }

    @Override
    public Version getServerVersion() {
        return serverVersion;
    }

    @Override
    public JavaPlugin getPlugin() {
        return plugin;
    }
}
