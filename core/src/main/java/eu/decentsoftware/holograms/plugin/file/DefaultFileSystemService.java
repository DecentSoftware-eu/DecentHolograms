package eu.decentsoftware.holograms.plugin.file;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DefaultFileSystemService implements FileSystemService {
    private final JavaPlugin plugin;

    public DefaultFileSystemService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public File getExpansionJarsDirectory() {
        return new File(getExpansionsDirectory(), "sources");
    }

    @Override
    public File getExpansionDataDirectory(String expansionId) {
        File expansionsContainer = new File(plugin.getDataFolder(), "expansions-data");

        return new File(expansionsContainer, expansionId);
    }

    private File getExpansionsDirectory() {
        return new File(plugin.getDataFolder(), "expansions");
    }
}
