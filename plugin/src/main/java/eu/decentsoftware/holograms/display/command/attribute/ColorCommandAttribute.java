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
import eu.decentsoftware.holograms.display.attribute.parser.ColorDisplayAttributeParser;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class ColorCommandAttribute<D> implements CommandAttribute {

    private static final ColorDisplayAttributeParser PARSER = new ColorDisplayAttributeParser();
    private final String name;
    private final BiConsumer<D, DisplayColor> applyValue;
    private final Class<D> applicableDisplayType;

    public ColorCommandAttribute(String name, BiConsumer<D, DisplayColor> applyValue, Class<D> applicableDisplayType) {
        this.name = name;
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
        return Arrays.asList("9000FF", "00FF00", "0000FF", "FFFF00", "FF0000", "00AAFF", "000000", "FFFFFF", "FF00FF00", "005500FF",
                "0000FFFF", "FFFF00FF", "FF0000FF", "00FF66FF", "00000000", "FF00FF00", "255,0,255", "0,255,0", "0,0,255", "255,255,0",
                "255,0,0", "0,255,255", "0,0,0", "255,255,255", "128,255,0,255", "255,0,255,255");
    }

    @Override
    public void applyValue(@NotNull DisplayBase display, @NotNull String value) {
        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new CommandAttributeValidationException("Attribute " + name + " is not applicable to this display type.");
        }
        DisplayColor color = PARSER.parseValue(value);
        if (color == null) {
            throw new CommandAttributeValidationException("Expected a HEX color (e.g. 9000FF or FF00FF00) or an ARGB/RGB value (e.g. 255,0,255 or 128,255,0,255).");
        }
        applyValue.accept(applicableDisplayType.cast(display), color);
    }
}
