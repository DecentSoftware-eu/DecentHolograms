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
import eu.decentsoftware.holograms.display.attribute.parser.BooleanDisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import org.jetbrains.annotations.NotNull;

public class TextShadowAttributeDefinition implements AttributeDefinition<Boolean> {

    private final BooleanDisplayAttributeParser parser = new BooleanDisplayAttributeParser();

    @Override
    public @NotNull String getName() {
        return "text-shadow";
    }

    @Override
    public @NotNull Class<Boolean> getValueType() {
        return Boolean.class;
    }

    @Override
    public @NotNull DisplayAttributeParser<Boolean> getParser() {
        return parser;
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.TEXT};
    }
}
