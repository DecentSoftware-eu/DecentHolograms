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

package eu.decentsoftware.holograms.display.attribute.value.display;

import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.StaticCompiledAttributeValue;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.platform.api.data.display.ItemDisplayType;

public final class ItemDisplayTypeValue implements AttributeValue<ItemDisplayType> {

    private final ItemDisplayType itemDisplayType;

    public ItemDisplayTypeValue(ItemDisplayType itemDisplayType) {
        this.itemDisplayType = itemDisplayType;
    }

    @Override
    public String getTypeKey() {
        return ItemDisplayTypeValueType.TYPE_ID;
    }

    @Override
    public CompiledAttributeValue<ItemDisplayType> compile(DisplayRenderContext context) {
        return new StaticCompiledAttributeValue<>(itemDisplayType);
    }

    @Override
    public String toHumanReadableString() {
        return itemDisplayType.name();
    }

    public ItemDisplayType getItemDisplayType() {
        return itemDisplayType;
    }
}
