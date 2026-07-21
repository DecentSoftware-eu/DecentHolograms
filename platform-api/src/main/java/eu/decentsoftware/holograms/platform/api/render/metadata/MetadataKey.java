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

package eu.decentsoftware.holograms.platform.api.render.metadata;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an immutable key used to define and manage metadata of a specific type.
 * The {@code MetadataKey} is parameterized by the type of value it can hold and
 * provides functionality for creating metadata values and comparing them using a
 * specified equality handler.
 *
 * @param <T> The type of value associated with the metadata key.
 * @author d0by
 * @since 2.10.0
 */
public final class MetadataKey<T> {

    private final MetadataType type;
    private final Class<T> valueType;

    /**
     * Creates a new metadata key with the specified type and value type.
     *
     * <p>The equality handler used to compare metadata values is {@link Object#equals(Object)}.
     *
     * @param type      The type of the metadata key.
     * @param valueType The type of the value associated with the metadata key.
     */
    public MetadataKey(@NotNull MetadataType type, @NotNull Class<T> valueType) {
        this.type = type;
        this.valueType = valueType;
    }

    /**
     * Creates a new metadata value for this key.
     *
     * @param value The initial value of the metadata value.
     * @return The created metadata value.
     */
    @NotNull
    public MetadataValue<T> createValue(T value) {
        return new MetadataValue<>(this, value);
    }

    /**
     * Gets the type of this metadata key.
     *
     * @return The type of this metadata key.
     * @see MetadataType
     * @see DisplayMetadataType
     */
    @NotNull
    public MetadataType getType() {
        return type;
    }

    /**
     * Gets the type of the value associated with this metadata key.
     *
     * @return The type.
     */
    @NotNull
    public Class<T> getValueType() {
        return valueType;
    }

    @Override
    public String toString() {
        return "MetadataKey[" + type + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MetadataKey)) {
            return false;
        }

        MetadataKey<?> other = (MetadataKey<?>) o;
        return type == other.type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }
}
