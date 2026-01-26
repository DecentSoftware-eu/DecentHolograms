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
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.parser.BrightnessDisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBrightness;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class BrightnessAttributeDefinition implements AttributeDefinition<DisplayBrightness> {

    public static final AttributeKey<DisplayBrightness> KEY = AttributeKey.of("brightness", DisplayBrightness.class);
    private final BrightnessDisplayAttributeParser parser = new BrightnessDisplayAttributeParser();

    @Override
    public @NotNull AttributeKey<DisplayBrightness> getKey() {
        return KEY;
    }

    @Override
    public @NotNull DisplayAttributeParser<DisplayBrightness> getParser() {
        return parser;
    }

    @Override
    public DisplayBrightness getDefaultValue() {
        return null;
    }

    @Override
    public @NotNull List<String> valueHints(CommandSender sender, String currentInput) {
        return Arrays.asList(
                "0,0",
                "5,5",
                "10,10",
                "15,15",
                "5,0",
                "10,0",
                "15,0",
                "0,5",
                "0,10",
                "0,15"
        );
    }

    @Override
    public String format(DisplayBrightness value) {
        if (value == null) {
            return null;
        }
        return value.getBlockLight() + "," + value.getSkyLight();
    }
}
