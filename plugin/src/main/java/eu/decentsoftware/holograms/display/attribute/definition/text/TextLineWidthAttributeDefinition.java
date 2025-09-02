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

package eu.decentsoftware.holograms.display.attribute.definition.text;

import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.IntegerDisplayAttributeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextLineWidthAttributeDefinition implements AttributeDefinition<Integer> {

    private final IntegerDisplayAttributeParser parser = new IntegerDisplayAttributeParser();

    @Override
    public @NotNull String getName() {
        return "line-width";
    }

    @Override
    public @NotNull Class<Integer> getValueType() {
        return Integer.class;
    }

    @Override
    public @NotNull DisplayAttributeParser<Integer> getParser() {
        return parser;
    }

    @Override
    public Integer getDefaultValue() {
        return 200;
    }

    @Override
    public @Nullable DisplayType[] getAllowedDisplayTypes() {
        return new DisplayType[]{DisplayType.TEXT};
    }
}
