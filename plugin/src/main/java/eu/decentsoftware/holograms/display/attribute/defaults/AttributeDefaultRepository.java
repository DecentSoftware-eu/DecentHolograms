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

package eu.decentsoftware.holograms.display.attribute.defaults;

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValueType;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValueTypeRegistry;
import eu.decentsoftware.holograms.display.config.YamlConfigurationLoaderFactory;
import eu.decentsoftware.holograms.display.config.dto.ConfigDefaultAttribute;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.resource.SaveResourceService;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handles loading of default attribute values from a YAML configuration file.
 *
 * @author d0by
 * @see AttributeDefaultService
 * @since 2.10.0
 */
public class AttributeDefaultRepository {

    private static final String DEFAULTS_FILE_NAME = "attribute-defaults.yml";

    private final YamlConfigurationLoaderFactory loaderFactory;
    private final SaveResourceService saveResourceService;
    private final AttributeDefinitionRegistry definitionRegistry;
    private final AttributeValueTypeRegistry valueTypeRegistry;
    private final Path pluginDirectory;

    public AttributeDefaultRepository(YamlConfigurationLoaderFactory loaderFactory,
                                      SaveResourceService saveResourceService,
                                      AttributeDefinitionRegistry definitionRegistry,
                                      AttributeValueTypeRegistry valueTypeRegistry,
                                      Path pluginDirectory) {
        this.loaderFactory = loaderFactory;
        this.saveResourceService = saveResourceService;
        this.definitionRegistry = definitionRegistry;
        this.valueTypeRegistry = valueTypeRegistry;
        this.pluginDirectory = pluginDirectory;
    }

    /**
     * Loads the default attribute value for the specified display type from a configuration file.
     * If no defaults are found, an empty map is returned.
     *
     * <p>This method also validates the loaded attributes against the registered definitions and value types,
     * ensuring that only valid defaults are returned.</p>
     *
     * @param displayType The type of display for which default attributes are to be loaded. Must not be null.
     * @return A map containing the loaded default attribute keys and their corresponding values.
     * An empty map is returned if no defaults are available or in case of an error.
     */
    public Map<AttributeKey<?>, AttributeValue<?>> loadDefaults(DisplayType displayType) {
        Path file = resolveDefaultsFile();
        if (Files.notExists(file)) {
            Log.info("Attribute defaults file not found. Creating default file: %s", file);
            saveResourceService.saveResource(DEFAULTS_FILE_NAME, false);
            if (Files.notExists(file)) {
                Log.error("Failed to create default attribute defaults file: %s", file);
                return Collections.emptyMap();
            }
        }

        try {
            CommentedConfigurationNode displayNode = loadDisplayNode(file, displayType);
            if (displayNode.virtual()) {
                Log.warn("No attribute defaults found for display type '%s'. Skipping.", displayType.name());
                return Collections.emptyMap();
            }

            Map<Object, CommentedConfigurationNode> attributeNodes = displayNode.childrenMap();
            if (attributeNodes.isEmpty()) {
                Log.warn("No attribute defaults found for display type '%s'. Skipping.", displayType.name());
                return Collections.emptyMap();
            }

            return attributeNodes.values().stream()
                    .map(node -> parseAttributeNode(node, displayType))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toMap(ResolvedAttribute::getKey, ResolvedAttribute::getValue));
        } catch (Exception e) {
            Log.error("Failed to load attribute defaults from file: %s", e, file);
            return Collections.emptyMap();
        }
    }

    private CommentedConfigurationNode loadDisplayNode(Path file, DisplayType displayType) throws ConfigurateException {
        YamlConfigurationLoader loader = loaderFactory.createLoader(file);
        CommentedConfigurationNode root = loader.load();
        return root.node("display").node(displayType.name());
    }

    private ResolvedAttribute parseAttributeNode(CommentedConfigurationNode node, DisplayType displayType) {
        String attributeName = extractAttributeName(node);
        if (attributeName == null) {
            return null;
        }

        try {
            ConfigDefaultAttribute configDefaultAttribute = node.get(ConfigDefaultAttribute.class);
            if (configDefaultAttribute == null) {
                Log.warn("Invalid attribute value in defaults file: %s", attributeName);
                return null;
            }

            if (!configDefaultAttribute.isEnabled()) {
                return null;
            }

            AttributeValue<?> value = configDefaultAttribute.getValue();
            if (value == null) {
                return null;
            }

            return validateAndResolve(attributeName, value, displayType);
        } catch (SerializationException e) {
            Log.warn("Failed to load value for attribute '%s' in defaults file.", e, attributeName);
            return null;
        }
    }

    private String extractAttributeName(CommentedConfigurationNode node) {
        Object key = node.key();
        if (key instanceof String) {
            return (String) key;
        }

        Log.warn("Invalid attribute name in defaults file: " + key);
        return null;
    }

    private ResolvedAttribute validateAndResolve(String attributeName, AttributeValue<?> value, DisplayType displayType) {
        AttributeDefinition<?> definition = definitionRegistry.getDefinitionByName(attributeName);
        if (definition == null) {
            Log.warn("Unknown attribute in defaults file: " + attributeName);
            return null;
        }

        if (!definition.applicableTo(displayType)) {
            Log.warn("Attribute '%s' is not applicable to display type '%s'.", attributeName, displayType.name());
            return null;
        }

        AttributeValueType<?, ?> valueType = valueTypeRegistry.getByTypeId(value.getTypeKey());
        if (valueType == null) {
            Log.warn("Unknown attribute value type in defaults file '%s' for attribute '%s'.",
                    value.getTypeKey(), attributeName);
            return null;
        }

        if (!valueType.getOutputType().equals(definition.getValueType())) {
            Log.warn("Incompatible attribute value type in defaults file '%s' for attribute '%s'.",
                    value.getTypeKey(), attributeName);
            return null;
        }

        return new ResolvedAttribute(definition.getKey(), value);
    }

    private Path resolveDefaultsFile() {
        return pluginDirectory.resolve(DEFAULTS_FILE_NAME);
    }

    private static class ResolvedAttribute {
        private final AttributeKey<?> key;
        private final AttributeValue<?> value;

        public ResolvedAttribute(AttributeKey<?> key, AttributeValue<?> value) {
            this.key = key;
            this.value = value;
        }

        public AttributeKey<?> getKey() {
            return key;
        }

        public AttributeValue<?> getValue() {
            return value;
        }
    }
}
