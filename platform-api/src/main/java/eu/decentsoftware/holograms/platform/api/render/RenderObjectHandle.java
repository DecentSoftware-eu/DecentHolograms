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

import java.util.Objects;

public final class RenderObjectHandle {

    private final String id;
    private final DisplayType displayType;

    public RenderObjectHandle(String id, DisplayType displayType) {
        this.id = id;
        this.displayType = displayType;
    }

    public String getId() {
        return id;
    }

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
