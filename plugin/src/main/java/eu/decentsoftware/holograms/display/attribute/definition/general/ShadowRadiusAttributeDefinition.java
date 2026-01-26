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

import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.AttributeValidationException;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.FloatDisplayAttributeParser;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class ShadowRadiusAttributeDefinition implements AttributeDefinition<Float> {

    public static final AttributeKey<Float> KEY = AttributeKey.of("shadow-radius", Float.class);
    private final FloatDisplayAttributeParser parser = new FloatDisplayAttributeParser();

    @Override
    public @NotNull AttributeKey<Float> getKey() {
        return KEY;
    }

    @Override
    public @NotNull DisplayAttributeParser<Float> getParser() {
        return parser;
    }

    @Override
    public Float getDefaultValue() {
        return 0.0f;
    }

    @Override
    public void validate(Float value) throws AttributeValidationException {
        if (value < 0.0f) {
            throw new AttributeValidationException("Shadow radius cannot be negative.");
        }
        if (value > 32.0f) {
            throw new AttributeValidationException("Shadow radius cannot be greater than 32.");
        }
    }

    @Override
    public @NotNull List<String> valueHints(CommandSender sender, String currentInput) {
        return Arrays.asList("0.0", "1.0", "2.0", "4.0", "8.0", "16.0", "32.0");
    }

    @Override
    public String format(Float value) {
        if (value == null) {
            return null;
        }
        return String.format("%.2f", value);
    }
}
