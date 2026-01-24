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

package eu.decentsoftware.holograms.display.attribute.definition.item;

import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.EnumDisplayAttributeParser;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayType;
import org.jetbrains.annotations.NotNull;

public class ItemDisplayTypeAttributeDefinition implements AttributeDefinition<ItemDisplayType> {

    private final EnumDisplayAttributeParser<ItemDisplayType> parser = new EnumDisplayAttributeParser<>(ItemDisplayType.class);

    @Override
    public @NotNull String getName() {
        return "display-type";
    }

    @Override
    public @NotNull Class<ItemDisplayType> getValueType() {
        return ItemDisplayType.class;
    }

    @Override
    public @NotNull DisplayAttributeParser<ItemDisplayType> getParser() {
        return parser;
    }

    @Override
    public ItemDisplayType getDefaultValue() {
        return ItemDisplayType.NONE;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.ITEM};
    }
}

