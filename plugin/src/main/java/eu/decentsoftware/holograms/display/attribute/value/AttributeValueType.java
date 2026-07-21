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

package eu.decentsoftware.holograms.display.attribute.value;

import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

/**
 * Represents a complete attribute value type definition.
 * This bundles the type ID, value class, factory, and serialization logic together.
 *
 * @param <V> The AttributeValue implementation type
 * @param <T> The output type this produces (e.g., DecentColor, Vector, etc.)
 */
public interface AttributeValueType<V extends AttributeValue<T>, T> {

    /**
     * @return The unique identifier for this value type (e.g., "rainbow-transition", "RGBA")
     */
    String getTypeId();

    /**
     * @return The class of the AttributeValue implementation
     */
    Class<V> getValueClass();

    /**
     * @return The output type this value produces
     */
    Class<T> getOutputType();

    /**
     * Serialize an AttributeValue to a configuration node.
     *
     * @param value The value to serialize
     * @param node  The configuration node to write to
     */
    void serialize(V value, ConfigurationNode node) throws SerializationException;

    /**
     * Deserialize an AttributeValue from a configuration node.
     *
     * @param node The configuration node to read from
     * @return The deserialized AttributeValue
     */
    V deserialize(ConfigurationNode node) throws SerializationException;
}