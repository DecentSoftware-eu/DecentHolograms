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

package eu.decentsoftware.holograms.display.command.attribute;

import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.StaticDisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.parser.EnumDisplayAttributeParser;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class EnumCommandAttribute<E extends Enum<?>, D extends DisplayBase> implements CommandAttribute {

    private final String name;
    private final Class<E> enumClass;
    private final BiConsumer<D, DisplayAttribute<E>> applyValue;
    private final Class<D> applicableDisplayType;
    private final EnumDisplayAttributeParser<E> parser;

    public EnumCommandAttribute(String name,
                                Class<E> enumClass,
                                BiConsumer<D, DisplayAttribute<E>> applyValue,
                                Class<D> applicableDisplayType) {
        this.name = name;
        this.enumClass = enumClass;
        this.applyValue = applyValue;
        this.applicableDisplayType = applicableDisplayType;
        this.parser = new EnumDisplayAttributeParser<>(enumClass);
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
        E enumValue = parser.parseValue(value);
        if (enumValue == null) {
            throw new CommandAttributeValidationException("Invalid value for attribute " + name + ". Expected one of: " +
                    Arrays.stream(enumClass.getEnumConstants())
                            .map(Object::toString)
                            .collect(Collectors.joining(", ")));
        }

        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new CommandAttributeValidationException("Attribute " + name + " is not applicable to this display type.");
        }

        this.applyValue.accept(applicableDisplayType.cast(display), new StaticDisplayAttribute<>(enumValue));
    }

    @Override
    public void resetValue(@NotNull DisplayBase display) {
        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new CommandAttributeValidationException("Attribute is not applicable to this display type.");
        }
        applyValue.accept(applicableDisplayType.cast(display), new StaticDisplayAttribute<>(null));
    }
}
