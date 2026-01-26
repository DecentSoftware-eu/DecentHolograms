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

import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.parser.ColorDisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class GlowColorAttributeDefinition implements AttributeDefinition<DisplayColor> {

    public static final AttributeKey<DisplayColor> KEY = AttributeKey.of("glow-color", DisplayColor.class);
    private final ColorDisplayAttributeParser parser = new ColorDisplayAttributeParser();

    @Override
    public @NotNull AttributeKey<DisplayColor> getKey() {
        return KEY;
    }

    @Override
    public @NotNull DisplayAttributeParser<DisplayColor> getParser() {
        return parser;
    }

    @Override
    public DisplayColor getDefaultValue() {
        return null;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.ITEM, DisplayType.BLOCK};
    }

    @Override
    public @NotNull List<String> valueHints(CommandSender sender, String currentInput) {
        return Arrays.asList("9000FF", "00FF00", "0000FF", "FFFF00", "FF0000", "00AAFF", "000000", "FFFFFF", "FF00FF00", "005500FF",
                "0000FFFF", "FFFF00FF", "FF0000FF", "00FF66FF", "00000000", "FF00FF00", "255,0,255", "0,255,0", "0,0,255", "255,255,0",
                "255,0,0", "0,255,255", "0,0,0", "255,255,255", "128,255,0,255", "255,0,255,255");
    }

    @Override
    public String format(DisplayColor value) {
        if (value == null) {
            return null;
        }
        return value.asRGBString() + value.asHex();
    }
}
