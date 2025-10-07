package eu.decentsoftware.holograms.api.utils.config;

import eu.decentsoftware.holograms.api.utils.Log;
import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * This class extends {@link YamlConfiguration} and is used to load and save
 * the configuration file. It also provides a method to reload the configuration
 * file. You can also use it as normal {@link YamlConfiguration}.
 *
 * @author d0by
 */
@Getter
public class FileConfig extends YamlConfiguration {

    protected final JavaPlugin plugin;
    protected final String path;
    protected final File file;

    /**
     * Creates a new instance of {@link FileConfig}.
     * <p>
     * This constructor also creates the file if it doesn't exist and
     * loads the configuration.
     * </p>
     *
     * @param plugin The plugin that this config belongs to.
     * @param path   The path to the file. Must be a relative path to .yml file.
     */
    public FileConfig(@NotNull JavaPlugin plugin, @NotNull String path) {
        this.plugin = plugin;
        this.path = path;
        this.file = new File(plugin.getDataFolder(), path);
        this.createFile();
        this.loadData();
    }

    /**
     * Creates a new instance of {@link FileConfig}.
     * <p>
     * This constructor also creates the file if it doesn't exist and
     * loads the configuration.
     * </p>
     *
     * @param plugin The plugin that this config belongs to.
     * @param file   The file to load. Must be a .yml file.
     */
    public FileConfig(@NotNull JavaPlugin plugin, @NotNull File file) {
        this.plugin = plugin;
        this.path = file.getName();
        this.file = file;
        this.createFile();
        this.loadData();
    }

    /**
     * Creates the file if it doesn't exist. If the file is also a resource,
     * it will be copied as the default configuration.
     */
    public void createFile() {
        if (!file.exists()) {
            plugin.getDataFolder().mkdirs();

            // If file isn't a resource, create from scratch
            if (plugin.getResource(this.path) == null) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Log.warn("Failed to create config file at path '%s'.", e, path);
                }
            } else {
                plugin.saveResource(this.path, false);
            }
        }
    }

    /**
     * Saves this configuration to the file.
     */
    public void saveData() {
        try {
            this.save(this.file);
        } catch (IOException e) {
            Log.warn("Failed to save config file at path '%s'.", e, path);
        }
    }

    /**
     * Reloads the configuration from the file.
     *
     * @deprecated For removal.
     */
    @Deprecated
    public void reload() {
        try {
            this.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Log.warn("Failed to reload config file at path '%s'.", path);
        }
    }

    private void loadData() {
        try {
            this.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Log.warn("Failed to load config file at path '%s'.", path);
        }
    }

    /**
     * Delete the file.
     */
    public void delete() {
        if (file.exists()) {
            try {
                Files.delete(file.toPath());
            } catch (IOException e) {
                Log.warn("Failed to delete config file at path '%s'.", e, path);
            }
        }
    }

}