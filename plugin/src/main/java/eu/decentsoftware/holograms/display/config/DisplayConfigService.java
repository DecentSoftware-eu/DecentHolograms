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
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import eu.decentsoftware.holograms.display.DecentLocation;
import eu.decentsoftware.holograms.display.config.dto.ConfigAttribute;
import eu.decentsoftware.holograms.display.config.dto.ConfigDisplay;
import eu.decentsoftware.holograms.display.config.serializer.ConfigAttributeSerializer;
import eu.decentsoftware.holograms.display.config.serializer.DecentLocationSerializer;
import eu.decentsoftware.holograms.display.config.serializer.DisplayBrightnessSerializer;
import eu.decentsoftware.holograms.display.config.serializer.DisplayColorSerializer;
import eu.decentsoftware.holograms.display.config.serializer.DisplayVector3fSerializer;
import eu.decentsoftware.holograms.display.config.serializer.HologramItemSerializer;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBrightness;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
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
    private final TypeSerializerCollection serializers;

    public DisplayConfigService(JavaPlugin plugin) {
        this.displaysDirectory = plugin.getDataFolder().toPath().resolve(DISPLAYS_DIR);
        this.serializers = createSerializers();
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
            YamlConfigurationLoader loader = createLoader(path);
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
        YamlConfigurationLoader loader = createLoader(path);
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

    private YamlConfigurationLoader createLoader(Path path) {
        return YamlConfigurationLoader.builder()
                .path(path)
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(options ->
                        options.serializers(serializerCollection -> serializerCollection.registerAll(serializers))
                )
                .build();
    }

    private static TypeSerializerCollection createSerializers() {
        return TypeSerializerCollection.builder()
                .register(DecentLocation.class, new DecentLocationSerializer())
                .register(DisplayVector3f.class, new DisplayVector3fSerializer())
                .register(DisplayColor.class, new DisplayColorSerializer())
                .register(DisplayBrightness.class, new DisplayBrightnessSerializer())
                .register(HologramItem.class, new HologramItemSerializer())
                .register(ConfigAttribute.class, new ConfigAttributeSerializer())
                .build();
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
