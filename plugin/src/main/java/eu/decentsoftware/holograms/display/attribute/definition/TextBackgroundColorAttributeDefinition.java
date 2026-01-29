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

package eu.decentsoftware.holograms.display.attribute.definition;

import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TextBackgroundColorAttributeDefinition implements AttributeDefinition<DisplayColor> {

    public static final AttributeKey<DisplayColor> KEY = AttributeKey.of("background-color", DisplayColor.class);
    private final DisplayColorAttributeCommandHandler commandHandler = new DisplayColorAttributeCommandHandler();

    @Override
    public @NotNull AttributeKey<DisplayColor> getKey() {
        return KEY;
    }

    @Override
    public DisplayColor getDefaultValue() {
        return DisplayColor.DEFAULT_BACKGROUND;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.TEXT};
    }

    @Override
    public String format(DisplayColor value) {
        if (value == null) {
            return null;
        }
        String rgbString = String.format("R: %s, G: %s, B: %s, A: %s", value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha());
        return value.asRGBString() + rgbString;
    }

    @Override
    public @NotNull DisplayColor parse(String[] args) {
        return commandHandler.parseColor(args);
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        return commandHandler.getHints(args);
    }
}
