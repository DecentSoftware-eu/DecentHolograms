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
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class FloatDisplayAttribute<D> implements DisplayAttribute {

    private final String name;
    private final float minValue;
    private final float maxValue;
    private final BiConsumer<D, Float> applyValue;
    private final Class<D> applicableDisplayType;

    public FloatDisplayAttribute(String name, BiConsumer<D, Float> applyValue, Class<D> applicableDisplayType) {
        this(name, Float.MIN_VALUE, Float.MAX_VALUE, applyValue, applicableDisplayType);
    }

    public FloatDisplayAttribute(String name, float minValue, float maxValue, BiConsumer<D, Float> applyValue, Class<D> applicableDisplayType) {
        this.name = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
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
        return Collections.emptyList();
    }

    @Override
    public void applyValue(@NotNull DisplayBase display, @NotNull String value) {
        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new DisplayAttributeValidationException("Attribute is not applicable to this display type.");
        }
        Float floatValue = parseValue(value);
        if (floatValue == null) {
            throw new DisplayAttributeValidationException("Expected a decimal number.");
        }
        if (floatValue < minValue || floatValue > maxValue) {
            throw new DisplayAttributeValidationException("Value out of range: " + value + ". (expected: " + minValue + " - " + maxValue + ")");
        }
        applyValue.accept(applicableDisplayType.cast(display), floatValue);
    }

    private Float parseValue(@NotNull String valueString) {
        try {
            return Float.parseFloat(valueString);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
