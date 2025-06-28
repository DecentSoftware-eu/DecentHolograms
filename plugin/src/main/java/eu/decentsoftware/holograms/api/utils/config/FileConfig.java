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
        this.reload();
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
        this.reload();
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
                    Log.warn("无法在路径 '%s' 创建配置文件。", e, path);
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
            Log.warn("无法保存路径为 '%s' 的配置文件。", e, path);
        }
    }

    /**
     * Reloads the configuration from the file.
     */
    public void reload() {
        try {
            this.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Log.warn("无法重新加载路径为 '%s' 的配置文件。", path);
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
                Log.warn("无法删除路径为 '%s' 的配置文件。", e, path);
            }
        }
    }

}