/*
 * This file is part of DecentHolograms, licensed under the GNU GPL v3.0 License.
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.display.config;

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.config.dto.ConfigDisplay;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DisplayConfigService {

    private static final String DISPLAYS_DIR = "displays";

    private final Path displaysDirectory;
    private final YamlConfigurationLoaderFactory loaderFactory;

    public DisplayConfigService(JavaPlugin plugin, YamlConfigurationLoaderFactory loaderFactory) {
        this.displaysDirectory = plugin.getDataFolder().toPath().resolve(DISPLAYS_DIR);
        this.loaderFactory = loaderFactory;
    }

    public List<ConfigDisplay> loadAll() {
        ensureDirectoryExists();

        try (Stream<Path> files = Files.list(displaysDirectory)) {
            return files
                    .filter(this::isYamlFile)
                    .map(this::loadSingleSafely)
                    .flatMap(optional -> optional.map(Stream::of).orElseGet(Stream::empty))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            Log.error("Failed to list display configs: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void save(String displayName, ConfigDisplay config) {
        Path path = resolve(displayName);

        try {
            YamlConfigurationLoader loader = loaderFactory.createLoader(path);
            ConfigurationNode root = loader.createNode();
            root.set(ConfigDisplay.class, config);
            loader.save(root);
        } catch (Exception e) {
            Log.error("Failed to save display '%s'.", e, displayName);
        }
    }

    public void delete(String displayName) {
        Path path = resolve(displayName);

        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            Log.error("Failed to delete display '%s'.", e, displayName);
        }
    }

    private Optional<ConfigDisplay> loadSingleSafely(Path path) {
        try {
            return Optional.of(loadSingle(path));
        } catch (Exception e) {
            Log.error("Failed to load display config '%s'.", e, path.getFileName());
            return Optional.empty();
        }
    }

    private ConfigDisplay loadSingle(Path path) throws IOException {
        YamlConfigurationLoader loader = loaderFactory.createLoader(path);
        ConfigurationNode root = loader.load();
        ConfigDisplay configDisplay = root.get(ConfigDisplay.class);
        if (configDisplay != null) {
            configDisplay.setName(getDisplayNameFromPath(path));
        }
        return configDisplay;
    }

    private String getDisplayNameFromPath(Path path) {
        return path.getFileName().toString().replace(".yml", "");
    }

    private void ensureDirectoryExists() {
        try {
            Files.createDirectories(displaysDirectory);
        } catch (IOException e) {
            throw new DisplayConfigException("Unable to create displays directory: " + displaysDirectory, e);
        }
    }

    private Path resolve(String displayName) {
        return displaysDirectory.resolve(displayName + ".yml");
    }

    private boolean isYamlFile(Path path) {
        String name = path.getFileName().toString();
        return name.endsWith(".yml");
    }
}
