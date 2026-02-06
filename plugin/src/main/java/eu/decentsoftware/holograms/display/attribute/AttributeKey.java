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

package eu.decentsoftware.holograms.display.attribute;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a unique key to identify an attribute.
 * This class is a generic and immutable structure that ties an attribute name with its corresponding type.
 *
 * @param <T> The type of the attribute value associated with this key.
 * @author d0by
 * @since 2.10.0
 */
public final class AttributeKey<T> {

    private final String name;
    private final Class<T> type;

    private AttributeKey(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Creates a new {@link AttributeKey} with the specified name and type.
     *
     * @param name The name of the attribute key. Must not be null.
     * @param type The type of the attribute value associated with the key. Must not be null.
     * @param <T>  The type parameter that represents the type of the attribute value.
     * @return An instance of {@link AttributeKey} containing the specified name and type.
     * @throws NullPointerException if the provided name or type is null.
     */
    @NotNull
    public static <T> AttributeKey<T> of(@NotNull String name, @NotNull Class<T> type) {
        Objects.requireNonNull(name, "name cannot be null");
        Objects.requireNonNull(type, "type cannot be null");
        return new AttributeKey<>(name, type);
    }

    /**
     * Get the name of the attribute key.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the type of the attribute value associated with this key.
     *
     * @return The type.
     */
    public Class<T> getType() {
        return type;
    }

    @Override
    public String toString() {
        return "AttributeKey[" + name + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AttributeKey)) {
            return false;
        }
        AttributeKey<?> that = (AttributeKey<?>) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}