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
import eu.decentsoftware.holograms.display.attribute.parser.IntegerDisplayAttributeParser;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class IntegerCommandAttribute<D> implements CommandAttribute {

    private static final IntegerDisplayAttributeParser PARSER = new IntegerDisplayAttributeParser();
    private final String name;
    private final int minValue;
    private final int maxValue;
    private final BiConsumer<D, DisplayAttribute<Integer>> applyValue;
    private final Function<D, DisplayAttribute<Integer>> getAttribute;
    private final Class<D> applicableDisplayType;

    public IntegerCommandAttribute(String name,
                                   BiConsumer<D, DisplayAttribute<Integer>> applyValue,
                                   Function<D, DisplayAttribute<Integer>> getAttribute,
                                   Class<D> applicableDisplayType) {
        this(name, Integer.MIN_VALUE, Integer.MAX_VALUE, applyValue, getAttribute, applicableDisplayType);
    }

    public IntegerCommandAttribute(String name,
                                   int minValue,
                                   int maxValue,
                                   BiConsumer<D, DisplayAttribute<Integer>> applyValue,
                                   Function<D, DisplayAttribute<Integer>> getAttribute,
                                   Class<D> applicableDisplayType) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
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
        return Collections.emptyList();
    }

    @Override
    public void applyValue(@NotNull DisplayBase display, @NotNull String value) {
        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new CommandAttributeValidationException("Attribute is not applicable to this display type.");
        }
        Integer parsedValue = PARSER.parseValue(value);
        if (parsedValue == null) {
            throw new CommandAttributeValidationException("Expected an integer.");
        }
        if (parsedValue < minValue || parsedValue > maxValue) {
            throw new CommandAttributeValidationException("Value out of range: " + parsedValue + ". (expected: " + minValue + " - " + maxValue + ")");
        }
        applyValue.accept(applicableDisplayType.cast(display), new StaticDisplayAttribute<>(getName(), parsedValue));
    }

    @Override
    public void resetValue(@NotNull DisplayBase display) {
        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new CommandAttributeValidationException("Attribute is not applicable to this display type.");
        }
        applyValue.accept(applicableDisplayType.cast(display), new StaticDisplayAttribute<>(getName(), null));
    }

    @Override
    public String getValue(@NotNull DisplayBase display) {
        DisplayAttribute<Integer> attribute = getAttribute.apply(applicableDisplayType.cast(display));
        if (attribute == null || attribute.getValue() == null) {
            return null;
        }
        return attribute.getValue().toString();
    }
}
