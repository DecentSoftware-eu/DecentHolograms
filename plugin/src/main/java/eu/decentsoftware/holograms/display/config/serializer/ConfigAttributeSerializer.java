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

import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValueType;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValueTypeRegistry;
import eu.decentsoftware.holograms.display.config.dto.ConfigAttribute;
import org.jspecify.annotations.Nullable;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

import java.lang.reflect.Type;

public final class ConfigAttributeSerializer implements TypeSerializer<ConfigAttribute> {

    private final AttributeValueTypeRegistry attributeValueTypeRegistry;

    public ConfigAttributeSerializer(AttributeValueTypeRegistry attributeValueTypeRegistry) {
        this.attributeValueTypeRegistry = attributeValueTypeRegistry;
    }

    @Override
    public ConfigAttribute deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String typeKey = node.node("value-type").getString();
        ConfigurationNode valueNode = node.node("value");
        ConfigAttribute configAttribute = new ConfigAttribute();
        configAttribute.setValueType(typeKey);
        configAttribute.setValue(deserializeValue(typeKey, valueNode));
        return configAttribute;
    }

    private <V extends AttributeValue<T>, T> V deserializeValue(String typeId, ConfigurationNode node) throws SerializationException {
        AttributeValueType<V, T> valueTypeDefinition = getAttributeValueTypeDefinition(typeId);
        return valueTypeDefinition.deserialize(node);
    }

    @Override
    public void serialize(Type type, @Nullable ConfigAttribute attribute, ConfigurationNode node) throws SerializationException {
        if (attribute == null || attribute.getValue() == null) {
            node.raw(null);
            return;
        }

        validateConfigAttribute(attribute);

        node.node("value-type").set(attribute.getValueType());
        ConfigurationNode valueNode = node.node("value");
        AttributeValue<?> value = attribute.getValue();
        serializeValue(attribute.getValueType(), value, valueNode);
    }

    private <V extends AttributeValue<T>, T> void serializeValue(String typeId, V value, ConfigurationNode node) throws SerializationException {
        AttributeValueType<V, T> valueTypeDefinition = getAttributeValueTypeDefinition(typeId);
        valueTypeDefinition.serialize(value, node);
    }

    private <V extends AttributeValue<T>, T> AttributeValueType<V, T> getAttributeValueTypeDefinition(String typeId) throws SerializationException {
        AttributeValueType<V, T> valueTypeDefinition = attributeValueTypeRegistry.getByTypeId(typeId);
        if (valueTypeDefinition == null) {
            throw new SerializationException("Unknown attribute value type: " + typeId);
        }
        return valueTypeDefinition;
    }

    private void validateConfigAttribute(ConfigAttribute attribute) throws SerializationException {
        String declaredTypeId = attribute.getValueType();
        AttributeValue<?> value = attribute.getValue();
        // Validate that the declared type matches the actual value class
        AttributeValueType<?, ?> valueTypeDefinition = getAttributeValueTypeDefinition(declaredTypeId);
        if (!valueTypeDefinition.getValueClass().equals(value.getClass())) {
            throw new SerializationException(String.format(
                    "Type mismatch: ConfigAttribute declares type '%s' (expects %s) but contains %s",
                    declaredTypeId,
                    valueTypeDefinition.getValueClass().getSimpleName(),
                    value.getClass().getSimpleName()
            ));
        }
    }
}
