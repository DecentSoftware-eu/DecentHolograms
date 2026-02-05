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
import org.jetbrains.annotations.NotNull;

/**
 * Represents a rendering operation to move an existing object to a new location.
 *
 * <p>This render intent encapsulates the new location that should be applied to the object.</p>
 *
 * <p>Instances of this class are immutable and carry the necessary data for the "move" rendering intent.</p>
 *
 * @author d0by
 * @see RenderIntent
 * @since 2.10.0
 */
public final class MoveRenderIntent implements RenderIntent {

    private final DecentLocation location;

    public MoveRenderIntent(@NotNull DecentLocation location) {
        this.location = location;
    }

    /**
     * Get the new location that should be applied to the object.
     *
     * @return The new location.
     */
    @NotNull
    public DecentLocation getLocation() {
        return location;
    }
}
