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
import eu.decentsoftware.holograms.display.attribute.parser.BooleanDisplayAttributeParser;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BooleanCommandAttribute<D> implements CommandAttribute {

    private static final BooleanDisplayAttributeParser PARSER = new BooleanDisplayAttributeParser();
    private final String name;
    private final BiConsumer<D, DisplayAttribute<Boolean>> applyValue;
    private final Function<D, DisplayAttribute<Boolean>> getAttribute;
    private final Class<D> applicableDisplayType;

    public BooleanCommandAttribute(String name,
                                   BiConsumer<D, DisplayAttribute<Boolean>> applyValue,
                                   Function<D, DisplayAttribute<Boolean>> getAttribute,
                                   Class<D> applicableDisplayType) {
        this.name = name;
        this.applyValue = applyValue;
        this.getAttribute = getAttribute;
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
        return Stream.of("true", "false")
                .filter(hint -> hint.startsWith(currentValueLowerCase))
                .collect(Collectors.toList());
    }

    @Override
    public void applyValue(@NotNull DisplayBase display, @NotNull String value) {
        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new CommandAttributeValidationException("Attribute is not applicable to this display type.");
        }
        Boolean parsedValue = PARSER.parseValue(value);
        applyValue.accept(applicableDisplayType.cast(display), new StaticDisplayAttribute<>(parsedValue));
    }

    @Override
    public void resetValue(@NotNull DisplayBase display) {
        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new CommandAttributeValidationException("Attribute is not applicable to this display type.");
        }
        applyValue.accept(applicableDisplayType.cast(display), new StaticDisplayAttribute<>(null));
    }

    @Override
    public String getValue(@NotNull DisplayBase display) {
        DisplayAttribute<Boolean> attribute = getAttribute.apply(applicableDisplayType.cast(display));
        if (attribute == null || attribute.getValue() == null) {
            return null;
        }
        return attribute.getValue().toString();
    }
}
