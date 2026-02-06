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

import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Represents a rendering operation for spawning a display at a specific location with associated metadata and content.
 *
 * <p>This class encapsulates the details required for the rendering system to create and render a Display object,
 * including its location in the world, metadata, and content.</p>
 *
 * <p>Instances of this class are immutable and encapsulate all necessary data for the "spawn display" rendering intent.</p>
 *
 * @author d0by
 * @see RenderIntent
 * @since 2.10.0
 */
public final class SpawnDisplayRenderIntent implements RenderIntent {

    private final DecentLocation location;
    private final Map<MetadataKey<?>, MetadataValue<?>> metadataValues;
    private final DisplayContent<?> content;

    public SpawnDisplayRenderIntent(@NotNull DecentLocation location,
                                    @NotNull Map<MetadataKey<?>, MetadataValue<?>> metadataValues,
                                    @NotNull DisplayContent<?> content) {
        this.location = location;
        this.metadataValues = metadataValues;
        this.content = content;
    }

    /**
     * Get the location at which the display should be spawned.
     *
     * @return The location.
     */
    @NotNull
    public DecentLocation getLocation() {
        return location;
    }

    /**
     * Get the metadata values associated with the display.
     *
     * @return The metadata values.
     */
    @NotNull
    public Map<MetadataKey<?>, MetadataValue<?>> getMetadataValues() {
        return metadataValues;
    }

    /**
     * Get the content of the display.
     *
     * @return The content.
     */
    @NotNull
    public DisplayContent<?> getContent() {
        return content;
    }
}
