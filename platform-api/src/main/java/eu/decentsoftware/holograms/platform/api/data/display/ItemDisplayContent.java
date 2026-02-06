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

package eu.decentsoftware.holograms.platform.api.data.display;

import eu.decentsoftware.holograms.platform.api.data.ItemDescriptor;

import java.util.Objects;

public final class ItemDisplayContent implements DisplayContent<ItemDescriptor> {

    private final ItemDescriptor descriptor;

    public ItemDisplayContent(ItemDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public ItemDescriptor getContent() {
        return descriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ItemDisplayContent)) {
            return false;
        }
        ItemDisplayContent that = (ItemDisplayContent) o;
        return Objects.equals(descriptor, that.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(descriptor);
    }
}
