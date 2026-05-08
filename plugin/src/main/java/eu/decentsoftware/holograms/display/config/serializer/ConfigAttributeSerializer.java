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

package eu.decentsoftware.holograms.display.config.serializer;

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValueSerializer;
import eu.decentsoftware.holograms.display.config.dto.ConfigAttribute;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class ConfigAttributeSerializer implements TypeSerializer<ConfigAttribute> {

    /**
     * Holds the name of the display that's currently being deserialized.
     * This value is used in error messages.
     *
     * @implNote This is an ugly solution, but I couldn't think of any other way to get the display name here.
     */
    private static final ThreadLocal<String> DISPLAY_NAME = new ThreadLocal<>();

    public static void setDisplayName(String displayName) {
        DISPLAY_NAME.set(displayName);
    }

    public static void clearDisplayName() {
        DISPLAY_NAME.remove();
    }

    private final AttributeValueSerializer attributeValueSerializer;

    public ConfigAttributeSerializer(AttributeValueSerializer attributeValueSerializer) {
        this.attributeValueSerializer = attributeValueSerializer;
    }

    @Override
    public ConfigAttribute deserialize(Type type, ConfigurationNode node) throws SerializationException {
        try {
            String typeKey = deserializeValueType(node);
            ConfigurationNode valueNode = getValueNode(node);
            ConfigAttribute configAttribute = new ConfigAttribute();
            configAttribute.setValueType(typeKey);
            configAttribute.setValue(attributeValueSerializer.deserialize(typeKey, valueNode));
            return configAttribute;
        } catch (SerializationException e) {
            String attributeName = getAttributeName(node);
            String displayName = DISPLAY_NAME.get();
            Log.warn("Failed to load attribute '%s' for display '%s': %s", attributeName, displayName, e.getMessage());
            return ConfigAttribute.INVALID;
        }
    }

    private String getAttributeName(ConfigurationNode node) {
        Object key = node.key();
        if (!(key instanceof String)) {
            return "unknown";
        }
        return (String) key;
    }

    private ConfigurationNode getValueNode(ConfigurationNode node) throws SerializationException {
        ConfigurationNode valueNode = node.node("value");
        if (valueNode.virtual()) {
            throw new SerializationException("Missing value.");
        }
        return valueNode;
    }

    private String deserializeValueType(ConfigurationNode node) throws SerializationException {
        ConfigurationNode valueTypeNode = node.node("value-type");
        if (valueTypeNode.virtual() || valueTypeNode.isNull()) {
            throw new SerializationException("Missing value type.");
        }
        return valueTypeNode.getString();
    }

    @Override
    public void serialize(Type type, @Nullable ConfigAttribute attribute, ConfigurationNode node) throws SerializationException {
        if (attribute == null || attribute.getValue() == null) {
            node.raw(null);
            return;
        }

        node.node("value-type").set(attribute.getValueType());
        ConfigurationNode valueNode = node.node("value");
        attributeValueSerializer.serialize(attribute.getValueType(), attribute.getValue(), valueNode);
    }
}
