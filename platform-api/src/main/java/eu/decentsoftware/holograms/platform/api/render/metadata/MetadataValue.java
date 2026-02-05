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
 * Represents a metadata value associated with a specific {@link MetadataKey}.
 *
 * @param <T> The type of the metadata value.
 * @author d0by
 * @see MetadataKey
 * @since 2.10.0
 */
public class MetadataValue<T> {

    private final MetadataKey<T> key;
    private final T value;

    MetadataValue(@NotNull MetadataKey<T> key, T value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the metadata key associated with this value.
     *
     * @return The metadata key.
     * @see MetadataKey
     * @since 2.10.0
     */
    @NotNull
    public MetadataKey<T> getKey() {
        return key;
    }

    /**
     * Get the value of this metadata value.
     *
     * @return The value.
     * @since 2.10.0
     */
    public T getValue() {
        return value;
    }

    /**
     * Is this metadata value animated?
     *
     * <p>The core updates animated metadata values every tick via post-processing.</p>
     *
     * @return True if this metadata value is animated, false otherwise.
     * @since 2.10.0
     */
    public boolean isAnimated() {
        return false;
    }
}
