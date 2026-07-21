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

package eu.decentsoftware.holograms.platform.api.render;

import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Represents a handle for a render object with a unique identifier.
 *
 * <p>This class is used to associate rendering operations with a specific object in the rendering system.</p>
 *
 * @author d0by
 * @see PlatformRenderService
 * @since 2.10.0
 */
public final class RenderObjectHandle {

    private final String id;
    private final DisplayType displayType;

    /**
     * Create a new render object handle.
     *
     * @param id          The unique identifier.
     * @param displayType The display type.
     */
    public RenderObjectHandle(@NotNull String id, @NotNull DisplayType displayType) {
        this.id = id;
        this.displayType = displayType;
    }

    /**
     * Get the unique identifier of this render object.
     *
     * @return The identifier.
     */
    @NotNull
    public String getId() {
        return id;
    }

    /**
     * Get the display type of this render object.
     *
     * @return The display type.
     * @see DisplayType
     * @since 2.10.0
     */
    @NotNull
    public DisplayType getDisplayType() {
        return displayType;
    }

    @Override
    public String toString() {
        return "RenderObjectHandle[" + id + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RenderObjectHandle)) {
            return false;
        }
        RenderObjectHandle that = (RenderObjectHandle) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
