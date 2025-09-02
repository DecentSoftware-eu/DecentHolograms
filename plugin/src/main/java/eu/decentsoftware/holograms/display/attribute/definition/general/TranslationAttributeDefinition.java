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

import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.Vector3fDisplayAttributeParser;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import org.jetbrains.annotations.NotNull;

public class TranslationAttributeDefinition implements AttributeDefinition<DisplayVector3f> {

    private final Vector3fDisplayAttributeParser parser = new Vector3fDisplayAttributeParser();
    private final DisplayVector3f defaultValue = new DisplayVector3f(0, 0, 0);

    @Override
    public @NotNull String getName() {
        return "translation";
    }

    @Override
    public @NotNull Class<DisplayVector3f> getValueType() {
        return DisplayVector3f.class;
    }

    @Override
    public @NotNull DisplayAttributeParser<DisplayVector3f> getParser() {
        return parser;
    }

    @Override
    public DisplayVector3f getDefaultValue() {
        return defaultValue;
    }
}
