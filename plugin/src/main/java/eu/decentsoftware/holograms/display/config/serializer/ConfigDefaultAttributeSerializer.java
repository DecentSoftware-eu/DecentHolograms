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

import eu.decentsoftware.holograms.display.attribute.value.AttributeValueSerializer;
import eu.decentsoftware.holograms.display.config.dto.ConfigDefaultAttribute;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class ConfigDefaultAttributeSerializer implements TypeSerializer<ConfigDefaultAttribute> {

    private final AttributeValueSerializer attributeValueSerializer;

    public ConfigDefaultAttributeSerializer(AttributeValueSerializer attributeValueSerializer) {
        this.attributeValueSerializer = attributeValueSerializer;
    }

    @Override
    public ConfigDefaultAttribute deserialize(Type type, ConfigurationNode node) throws SerializationException {
        if (node.isNull()) {
            return null;
        }

        boolean enabled = node.node("enabled").getBoolean(true);
        String typeKey = node.node("value-type").getString();
        ConfigurationNode valueNode = node.node("value");
        ConfigDefaultAttribute configAttribute = new ConfigDefaultAttribute();
        configAttribute.setEnabled(enabled);
        configAttribute.setValueType(typeKey);
        configAttribute.setValue(attributeValueSerializer.deserialize(typeKey, valueNode));
        return configAttribute;
    }

    @Override
    public void serialize(Type type, @Nullable ConfigDefaultAttribute attribute, ConfigurationNode node) throws SerializationException {
        if (attribute == null || attribute.getValue() == null) {
            node.raw(null);
            return;
        }

        node.node("enabled").set(attribute.isEnabled());
        node.node("value-type").set(attribute.getValueType());
        ConfigurationNode valueNode = node.node("value");
        attributeValueSerializer.serialize(attribute.getValueType(), attribute.getValue(), valueNode);
    }
}