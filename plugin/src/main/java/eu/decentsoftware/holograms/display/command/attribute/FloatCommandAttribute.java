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
import eu.decentsoftware.holograms.display.attribute.parser.FloatDisplayAttributeParser;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FloatCommandAttribute<D> implements CommandAttribute {

    private static final FloatDisplayAttributeParser PARSER = new FloatDisplayAttributeParser();
    private final String name;
    private final float minValue;
    private final float maxValue;
    private final BiConsumer<D, DisplayAttribute<Float>> applyValue;
    private final Function<D, DisplayAttribute<Float>> getAttribute;
    private final Class<D> applicableDisplayType;

    public FloatCommandAttribute(String name,
                                 BiConsumer<D, DisplayAttribute<Float>> applyValue,
                                 Function<D, DisplayAttribute<Float>> getAttribute,
                                 Class<D> applicableDisplayType) {
        this(name, -Float.MAX_VALUE, Float.MAX_VALUE, applyValue, getAttribute, applicableDisplayType);
    }

    public FloatCommandAttribute(String name,
                                 float minValue,
                                 float maxValue,
                                 BiConsumer<D, DisplayAttribute<Float>> applyValue,
                                 Function<D, DisplayAttribute<Float>> getAttribute,
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
        Float floatValue = PARSER.parseValue(value);
        if (floatValue == null) {
            throw new CommandAttributeValidationException("Expected a decimal number.");
        }
        if (floatValue < minValue || floatValue > maxValue) {
            throw new CommandAttributeValidationException("Value out of range: " + value + ". (expected: " + minValue + " - " + maxValue + ")");
        }
        applyValue.accept(applicableDisplayType.cast(display), new StaticDisplayAttribute<>(floatValue));
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
        DisplayAttribute<Float> attribute = getAttribute.apply(applicableDisplayType.cast(display));
        if (attribute == null || attribute.getValue() == null) {
            return null;
        }
        return attribute.getValue().toString();
    }
}
