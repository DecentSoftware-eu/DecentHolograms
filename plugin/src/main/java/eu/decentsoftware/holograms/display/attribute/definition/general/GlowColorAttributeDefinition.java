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

package eu.decentsoftware.holograms.display.attribute.definition.general;

import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.parser.ColorDisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GlowColorAttributeDefinition implements AttributeDefinition<DisplayColor> {

    private final ColorDisplayAttributeParser parser = new ColorDisplayAttributeParser();

    @Override
    public @NotNull String getName() {
        return "glow-color";
    }

    @Override
    public @NotNull Class<DisplayColor> getValueType() {
        return DisplayColor.class;
    }

    @Override
    public @NotNull DisplayAttributeParser<DisplayColor> getParser() {
        return parser;
    }

    @Override
    public DisplayColor getDefaultValue() {
        return null;
    }

    @Override
    public @Nullable DisplayType[] getAllowedDisplayTypes() {
        return new DisplayType[]{DisplayType.ITEM, DisplayType.BLOCK};
    }
}
