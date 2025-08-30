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
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorDisplayAttribute<D> implements DisplayAttribute {

    private static final Pattern RGB_HEX_COLOR_PATTERN = Pattern.compile("[0-9a-fA-F]{6}");
    private static final Pattern ARGB_HEX_COLOR_PATTERN = Pattern.compile("[0-9a-fA-F]{8}");
    private static final Pattern ARGB_PATTERN = Pattern.compile("((?<a>\\d{1,3}),)?(?<r>\\d{1,3}),(?<g>\\d{1,3}),(?<b>\\d{1,3})");
    private final String name;
    private final BiConsumer<D, DisplayColor> applyValue;
    private final Class<D> applicableDisplayType;

    public ColorDisplayAttribute(String name, BiConsumer<D, DisplayColor> applyValue, Class<D> applicableDisplayType) {
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
    public void applyValue(@NotNull DisplayBase<?> display, @NotNull String value) {
        if (!applicableDisplayType.isAssignableFrom(display.getClass())) {
            throw new DisplayAttributeValidationException("Attribute " + name + " is not applicable to this display type.");
        }
        DisplayColor color = parseValue(value);
        if (color == null) {
            throw new DisplayAttributeValidationException("Expected a HEX color (e.g. 9000FF or FF00FF00) or an ARGB/RGB value (e.g. 255,0,255 or 128,255,0,255).");
        }
        applyValue.accept(applicableDisplayType.cast(display), color);
    }

    private DisplayColor parseValue(@NotNull String valueString) {
        int length = valueString.length();
        if (length == 6 && RGB_HEX_COLOR_PATTERN.matcher(valueString).matches()) {
            return parseFromRgbHex(valueString);
        } else if (length == 8 && ARGB_HEX_COLOR_PATTERN.matcher(valueString).matches()) {
            return parseFromArgbHex(valueString);
        }
        Matcher matcher = ARGB_PATTERN.matcher(valueString);
        if (matcher.matches()) {
            return parseFromArgb10(matcher);
        }
        return null;
    }

    private DisplayColor parseFromArgb10(Matcher matcher) {
        int a;
        if (matcher.group("a") != null) {
            a = Integer.parseInt(matcher.group("a"));
        } else {
            a = 255;
        }
        int r = Integer.parseInt(matcher.group("r"));
        int g = Integer.parseInt(matcher.group("g"));
        int b = Integer.parseInt(matcher.group("b"));
        if (a < 0 || a > 255 || r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
            return null;
        }
        return new DisplayColor(a, r, g, b);
    }

    private DisplayColor parseFromArgbHex(String hex) {
        try {
            int color = Integer.parseUnsignedInt(hex, 16);
            return DisplayColor.fromARGB(color);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private DisplayColor parseFromRgbHex(String hex) {
        try {
            int color = Integer.parseUnsignedInt(hex, 16);
            return DisplayColor.fromRGB(color);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
