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

import java.util.HashMap;
import java.util.Map;

public class AttributeValueTypeRegistry {

    private final Map<String, AttributeValueType<?, ?>> byTypeId = new HashMap<>();
    private final Map<Class<? extends AttributeValue<?>>, AttributeValueType<?, ?>> byValueClass = new HashMap<>();

    /**
     * Register an attribute value type.
     *
     * @param definition The definition to register.
     * @param <V>        The attribute value type.
     * @param <T>        The underlying value type.
     * @throws IllegalArgumentException If the type ID or value class is already registered.
     * @see AttributeValueType
     */
    public <V extends AttributeValue<T>, T> void register(AttributeValueType<V, T> definition) {
        String typeId = definition.getTypeId();
        Class<V> valueClass = definition.getValueClass();

        if (byTypeId.containsKey(typeId)) {
            throw new IllegalArgumentException("Type ID already registered: " + typeId);
        }
        if (byValueClass.containsKey(valueClass)) {
            throw new IllegalArgumentException("Value class already registered: " + valueClass.getName());
        }

        byTypeId.put(typeId, definition);
        byValueClass.put(valueClass, definition);
    }

    @SuppressWarnings("unchecked")
    public <V extends AttributeValue<T>, T> AttributeValueType<V, T> getByTypeId(String typeId) {
        AttributeValueType<?, ?> definition = byTypeId.get(typeId);
        if (definition == null) {
            return null;
        }
        return (AttributeValueType<V, T>) definition;
    }

    public AttributeValueType<? extends AttributeValue<?>, ?> getByTypeIdUnsafe(String typeId) {
        return byTypeId.get(typeId);
    }
}