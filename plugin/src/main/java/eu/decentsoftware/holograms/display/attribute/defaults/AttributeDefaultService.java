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
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class for managing and applying default attribute values to displays.
 *
 * @author d0by
 * @since 2.10.0
 */
public class AttributeDefaultService {

    private final AttributeDefaultRegistry registry;
    private final AttributeDefinitionRegistry definitionRegistry;
    private final AttributeDefaultRepository repository;

    public AttributeDefaultService(AttributeDefaultRegistry registry,
                                   AttributeDefinitionRegistry definitionRegistry,
                                   AttributeDefaultRepository repository) {
        this.registry = registry;
        this.definitionRegistry = definitionRegistry;
        this.repository = repository;
    }

    /**
     * Reloads the default attribute values from the repository and updates the registry accordingly.
     */
    public void reload() {
        registry.clear();
        loadDefaults();
    }

    /**
     * Clears all registered default attribute values.
     */
    public void shutdown() {
        registry.clear();
    }

    /**
     * Retrieves the default value for a specified attribute key and display type.
     *
     * <p>This method first attempts to retrieve a default value configured in the registry.
     * If no configured default value is found, it retrieves the definition of the attribute
     * using the provided key and fetches the default value from the definition.</p>
     *
     * @param <T>          The type of the attribute value.
     * @param displayType  The type of display for which the default value is being retrieved. Must not be null.
     * @param attributeKey The key that identifies the attribute whose default value is being retrieved. Must not be null.
     * @return An Optional containing the default value if found, or an empty Optional if no default value is available
     * for the given key and display type.
     * @since 2.10.0
     */
    public <T> Optional<AttributeValue<T>> getDefaultValue(DisplayType displayType, AttributeKey<T> attributeKey) {
        AttributeValue<T> configuredDefaultValue = registry.getDefaultValue(displayType, attributeKey);
        if (configuredDefaultValue != null) {
            return Optional.of(configuredDefaultValue);
        }

        AttributeDefinition<T> definition = definitionRegistry.getDefinitionByKey(attributeKey);
        if (definition == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(definition.getDefaultValue());
    }

    /**
     * Applies default attribute values to a specified display based on its display type.
     * This method retrieves all applicable attribute definitions for the given display type
     * and ensures that each attribute has its default value applied if applicable.
     *
     * <p>This method will only apply a default value if the attribute is not already set.
     * If the attribute is already set, this method will not override the existing value.</p>
     *
     * @param display The display to which the default attribute values will be applied. Must not be null.
     * @see #applyDefaultValue(DisplayBase, AttributeDefinition)
     * @since 2.10.0
     */
    public void applyDefaultValues(DisplayBase display) {
        DisplayType displayType = display.getType();
        List<AttributeDefinition<?>> applicableAttributes = definitionRegistry.getDefinitionsByDisplayType(displayType);

        for (AttributeDefinition<?> applicableAttribute : applicableAttributes) {
            applyDefaultValue(display, applicableAttribute);
        }
    }

    /**
     * Applies a default value to the specified display if the attribute is not already set.
     *
     * <p>This method will only apply a default value if the attribute is not already set.
     * If the attribute is already set, this method will not override the existing value.</p>
     *
     * @param <T>                 The type of the attribute value.
     * @param display             The display to which the default value will be applied. Must not be null.
     * @param attributeDefinition The definition of the attribute that contains the key and default value. Must not be null.
     * @since 2.10.0
     */
    public <T> void applyDefaultValue(DisplayBase display, AttributeDefinition<T> attributeDefinition) {
        AttributeKey<T> attributeKey = attributeDefinition.getKey();
        if (display.hasAttribute(attributeKey)) {
            return;
        }

        setToDefaultValue(display, attributeDefinition);
    }

    /**
     * Sets the specified attribute of a display to its default value if a default value exists.
     *
     * <p>This method will set the attribute to its default value regardless of whether it is already set or not.
     * If the attribute is already set, this method will overwrite the existing value.</p>
     *
     * @param <T>                 The type of the attribute value.
     * @param display             The display to which the default value will be applied. Must not be null.
     * @param attributeDefinition The definition of the attribute that contains the key and default value. Must not be null.
     * @since 2.10.0
     */
    public <T> void setToDefaultValue(DisplayBase display, AttributeDefinition<T> attributeDefinition) {
        AttributeValue<T> defaultValue = getDefaultValue(display.getType(), attributeDefinition);
        AttributeKey<T> attributeKey = attributeDefinition.getKey();
        DisplayAttribute<T> displayAttribute = new DisplayAttribute<>(attributeKey, defaultValue);

        display.setAttribute(attributeKey, displayAttribute);
    }

    private <T> AttributeValue<T> getDefaultValue(DisplayType displayType, AttributeDefinition<T> attributeDefinition) {
        AttributeValue<T> configuredDefaultValue = registry.getDefaultValue(displayType, attributeDefinition.getKey());
        if (configuredDefaultValue != null) {
            return configuredDefaultValue;
        }
        return attributeDefinition.getDefaultValue();
    }

    private void loadDefaults() {
        Log.info("Loading default attribute values...");
        for (DisplayType displayType : DisplayType.values()) {
            loadDefaults(displayType);
        }
        Log.info("Loaded %d default attribute values!", registry.size());
    }

    private void loadDefaults(DisplayType displayType) {
        Map<AttributeKey<?>, AttributeValue<?>> defaultAttributes = repository.loadDefaults(displayType);
        for (AttributeKey<?> attributeKey : defaultAttributes.keySet()) {
            registerAttributeTypeSafe(displayType, attributeKey, defaultAttributes.get(attributeKey));
        }
    }

    @SuppressWarnings("unchecked") // Validated in AttributeDefaultRepository
    private <T> void registerAttributeTypeSafe(DisplayType displayType, AttributeKey<T> attributeKey, AttributeValue<?> value) {
        AttributeValue<T> typedValue = (AttributeValue<T>) value;
        registry.register(displayType, attributeKey, typedValue);
    }
}
