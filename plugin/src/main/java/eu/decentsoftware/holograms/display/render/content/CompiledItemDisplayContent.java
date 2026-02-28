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

package eu.decentsoftware.holograms.display.render.content;

import eu.decentsoftware.holograms.platform.api.data.ItemDescriptor;

public final class CompiledItemDisplayContent implements CompiledDisplayContent<ItemDescriptor> {

    private final ItemDescriptor descriptor;
    private boolean dirty;

    public CompiledItemDisplayContent(ItemDescriptor descriptor) {
        this.descriptor = descriptor;
        this.dirty = true;
    }

    @Override
    public ItemDescriptor getContent() {
        if (dirty) {
            dirty = false;
        }
        return descriptor;
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }
}
