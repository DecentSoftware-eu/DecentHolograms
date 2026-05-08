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

import org.spongepowered.configurate.serialize.TypeSerializerCollection;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

/**
 * A factory responsible for creating instances of {@link YamlConfigurationLoader}.
 *
 * <p>This factory centralizes the configuration of YAML loaders, ensuring that all loaders
 * created through it share the same settings, such as indentation, node style, and registered type serializers.</p>
 *
 * @author d0by
 * @since 2.10.0
 */
public final class YamlConfigurationLoaderFactory {

    private final TypeSerializerCollection typeSerializerCollection;

    public YamlConfigurationLoaderFactory(TypeSerializerCollection typeSerializerCollection) {
        this.typeSerializerCollection = typeSerializerCollection;
    }

    /**
     * Creates a new {@link YamlConfigurationLoader} configured for the specified file path.
     *
     * @param path the file path where the configuration loader reads from or writes to
     * @return a configured instance of {@link YamlConfigurationLoader}
     * @since 2.10.0
     */
    public YamlConfigurationLoader createLoader(Path path) {
        return YamlConfigurationLoader.builder()
                .path(path)
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(options ->
                        options.serializers(serializerCollection -> serializerCollection.registerAll(typeSerializerCollection))
                )
                .build();
    }
}
