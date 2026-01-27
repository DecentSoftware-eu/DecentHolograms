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

package eu.decentsoftware.holograms.display.attribute.parser;

import org.jetbrains.annotations.NotNull;

public class EnumDisplayAttributeParser<E extends Enum<?>> implements DisplayAttributeParser<E> {

    private final Class<E> enumClass;

    public EnumDisplayAttributeParser(Class<E> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public E parseValue(@NotNull String valueString) {
        for (E constant : enumClass.getEnumConstants()) {
            if (constant.toString().equalsIgnoreCase(valueString)) {
                return constant;
            }
        }
        throw new DisplayAttributeParseException("Unknown enum constant: " + valueString);
    }
}
