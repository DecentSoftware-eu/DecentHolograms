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

package eu.decentsoftware.holograms.platform.api.render.intent;

import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a rendering operation for updating a specific metadata value associated with an existing display element.
 *
 * <p>This render intent encapsulates the key and the new value that should be applied to the display.</p>
 *
 * <p>Instances of this class are immutable and carry the necessary data for the "update metadata" rendering intent.</p>
 *
 * @param <T> The type of the metadata value associated with the key.
 * @author d0by
 * @see RenderIntent
 * @since 2.10.0
 */
public final class UpdateMetadataRenderIntent<T> implements RenderIntent {

    private final MetadataKey<T> key;
    private final MetadataValue<T> value;

    public UpdateMetadataRenderIntent(@NotNull MetadataKey<T> key, @NotNull MetadataValue<T> value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Get the metadata key for which the value should be updated.
     *
     * @return The metadata key.
     */
    @NotNull
    public MetadataKey<T> getKey() {
        return key;
    }

    /**
     * Get the new value that should be applied to the display.
     *
     * @return The new value.
     */
    @NotNull
    public MetadataValue<T> getValue() {
        return value;
    }
}
