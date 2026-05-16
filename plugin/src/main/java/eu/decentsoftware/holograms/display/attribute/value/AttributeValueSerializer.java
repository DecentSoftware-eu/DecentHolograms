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

public class AttributeValueSerializer {

    private final AttributeValueTypeRegistry attributeValueTypeRegistry;

    public AttributeValueSerializer(AttributeValueTypeRegistry attributeValueTypeRegistry) {
        this.attributeValueTypeRegistry = attributeValueTypeRegistry;
    }

    public <V extends AttributeValue<T>, T> V deserialize(String typeId, ConfigurationNode node) throws SerializationException {
        AttributeValueType<V, T> valueTypeDefinition = getAttributeValueTypeDefinition(typeId);
        return valueTypeDefinition.deserialize(node);
    }

    public <V extends AttributeValue<T>, T> void serialize(String typeId, V value, ConfigurationNode node) throws SerializationException {
        AttributeValueType<V, T> valueTypeDefinition = getAttributeValueTypeDefinition(typeId);
        validateAttributeValueType(typeId, value, valueTypeDefinition);

        valueTypeDefinition.serialize(value, node);
    }

    private <V extends AttributeValue<T>, T> AttributeValueType<V, T> getAttributeValueTypeDefinition(String typeId) throws SerializationException {
        AttributeValueType<V, T> valueTypeDefinition = attributeValueTypeRegistry.getByTypeId(typeId);
        if (valueTypeDefinition == null) {
            throw new SerializationException("Unknown attribute value type: " + typeId);
        }
        return valueTypeDefinition;
    }

    private void validateAttributeValueType(String declaredTypeId,
                                            AttributeValue<?> value,
                                            AttributeValueType<?, ?> valueTypeDefinition) throws SerializationException {
        if (!valueTypeDefinition.getValueClass().equals(value.getClass())) {
            throw new SerializationException(String.format(
                    "Attribute value type mismatch: Declared type '%s' (expects %s) but got %s",
                    declaredTypeId,
                    valueTypeDefinition.getValueClass().getSimpleName(),
                    value.getClass().getSimpleName()
            ));
        }
    }
}
