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

package eu.decentsoftware.holograms.display.attribute;

import eu.decentsoftware.holograms.display.DisplayBase;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class EnumDisplayAttribute<E, D extends DisplayBase> implements DisplayAttribute {

    private final String name;
    private final Class<E> enumClass;
    private final BiConsumer<D, E> applyValue;
    private final Class<D> applicableDisplayType;

    public EnumDisplayAttribute(String name,
                                Class<E> enumClass,
                                BiConsumer<D, E> applyValue,
                                Class<D> applicableDisplayType) {
        this.name = name;
        this.enumClass = enumClass;
        this.applyValue = applyValue;
        this.applicableDisplayType = applicableDisplayType;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getValueHints(@NotNull CommandSender sender, @NotNull String currentString) {
        String currentValueLowerCase = currentString.toLowerCase();
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Object::toString)
                .filter(hint -> hint.toLowerCase().startsWith(currentValueLowerCase))
                .collect(Collectors.toList());
    }

    @Override
    public void applyValue(@NotNull DisplayBase display, @NotNull String value) {
        E enumValue = this.parseValue(value);
        if (enumValue == null) {
            throw new DisplayAttributeValidationException("Invalid value for attribute " + name + ". Expected one of: " +
                    Arrays.stream(enumClass.getEnumConstants())
                            .map(Object::toString)
                            .collect(Collectors.joining(", ")));
        }

        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new DisplayAttributeValidationException("Attribute " + name + " is not applicable to this display type.");
        }

        this.applyValue.accept(applicableDisplayType.cast(display), enumValue);
    }

    private E parseValue(@NotNull String valueString) {
        for (E constant : enumClass.getEnumConstants()) {
            if (constant.toString().equalsIgnoreCase(valueString)) {
                return constant;
            }
        }
        return null;
    }
}
