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
import eu.decentsoftware.holograms.display.attribute.parser.BrightnessDisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParseException;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBrightness;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class BrightnessCommandAttribute implements CommandAttribute {

    private static final BrightnessDisplayAttributeParser PARSER = new BrightnessDisplayAttributeParser();
    private final BiConsumer<DisplayBase, DisplayAttribute<DisplayBrightness>> applyValue;
    private final Function<DisplayBase, DisplayAttribute<DisplayBrightness>> getAttribute;

    public BrightnessCommandAttribute(BiConsumer<DisplayBase, DisplayAttribute<DisplayBrightness>> applyValue,
                                      Function<DisplayBase, DisplayAttribute<DisplayBrightness>> getAttribute) {
        this.applyValue = applyValue;
        this.getAttribute = getAttribute;
    }

    @NotNull
    @Override
    public String getName() {
        return "brightness";
    }

    @Override
    public List<String> getValueHints(@NotNull CommandSender sender, @NotNull String currentString) {
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
    public void applyValue(@NotNull DisplayBase display, @NotNull String value) {
        try {
            DisplayBrightness brightness = PARSER.parseValue(value);
            applyValue.accept(display, new StaticDisplayAttribute<>(getName(), brightness));
        } catch (DisplayAttributeParseException e) {
            throw new CommandAttributeValidationException(e.getMessage());
        }
    }

    @Override
    public void resetValue(@NotNull DisplayBase display) {
        applyValue.accept(display, new StaticDisplayAttribute<>(getName(), null));
    }

    @Override
    public String getValue(@NotNull DisplayBase display) {
        DisplayAttribute<DisplayBrightness> attribute = getAttribute.apply(display);
        if (attribute == null || attribute.getValue() == null) {
            return null;
        }
        DisplayBrightness value = attribute.getValue();
        return value.getBlockLight() + "," + value.getSkyLight();
    }
}
