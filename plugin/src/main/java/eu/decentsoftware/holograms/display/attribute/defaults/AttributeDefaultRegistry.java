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

import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Registry for default attribute values per display type.
 *
 * @author d0by
 * @since 2.10.0
 */
public class AttributeDefaultRegistry {

    private final Map<DisplayType, AttributeDefaultValueHolder> defaultValueHolder = new EnumMap<>(DisplayType.class);

    /**
     * Sets a default value for a specific attribute key and display type.
     *
     * <p>If a default value already exists for the given combination, it will be overwritten.</p>
     *
     * @param displayType  the display type for which to set the default value
     * @param attributeKey the attribute key for which to set the default value
     * @param defaultValue the default value to set
     */
    public <T> void register(DisplayType displayType, AttributeKey<T> attributeKey, AttributeValue<T> defaultValue) {
        AttributeDefaultValueHolder holder = defaultValueHolder.computeIfAbsent(displayType,
                dt -> new AttributeDefaultValueHolder());
        holder.setDefaultValue(attributeKey, defaultValue);
    }

    /**
     * Retrieves the default value for a specific attribute key and display type.
     *
     * @param displayType  the display type for which to retrieve the default value
     * @param attributeKey the attribute key for which to retrieve the default value
     * @return the default value, or null if no default value is registered
     */
    public <T> AttributeValue<T> getDefaultValue(DisplayType displayType, AttributeKey<T> attributeKey) {
        AttributeDefaultValueHolder holder = defaultValueHolder.get(displayType);
        if (holder == null) {
            return null;
        }
        return holder.getDefaultValue(attributeKey);
    }

    /**
     * Retrieves all default values for a specific display type.
     *
     * @param displayType the display type for which to retrieve default values
     * @return an unmodifiable map of attribute keys to default values, or an empty map if none exist
     */
    public Map<AttributeKey<?>, AttributeValue<?>> getAllDefaultValues(DisplayType displayType) {
        AttributeDefaultValueHolder holder = defaultValueHolder.get(displayType);
        if (holder == null) {
            return Collections.emptyMap();
        }
        return holder.getAllDefaultValues();
    }

    /**
     * Clears all registered default values for all display types.
     */
    public void clear() {
        defaultValueHolder.clear();
    }

    private static class AttributeDefaultValueHolder {
        private final Map<AttributeKey<?>, AttributeValue<?>> defaultValues = new ConcurrentHashMap<>();

        private <T> void setDefaultValue(AttributeKey<T> attributeKey, AttributeValue<T> defaultValue) {
            defaultValues.put(attributeKey, defaultValue);
        }

        @SuppressWarnings("unchecked") // Safe because we control registration
        private <T> AttributeValue<T> getDefaultValue(AttributeKey<T> attributeKey) {
            return (AttributeValue<T>) defaultValues.get(attributeKey);
        }

        private Map<AttributeKey<?>, AttributeValue<?>> getAllDefaultValues() {
            return Collections.unmodifiableMap(defaultValues);
        }
    }
}
