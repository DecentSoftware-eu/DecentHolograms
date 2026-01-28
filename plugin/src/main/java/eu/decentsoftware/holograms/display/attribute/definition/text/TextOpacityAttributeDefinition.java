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

package eu.decentsoftware.holograms.display.attribute.definition.text;

import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.AttributeParseException;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextOpacityAttributeDefinition implements AttributeDefinition<Integer> {

    public static final AttributeKey<Integer> KEY = AttributeKey.of("text-opacity", Integer.class);

    @Override
    public @NotNull AttributeKey<Integer> getKey() {
        return KEY;
    }

    @Override
    public Integer getDefaultValue() {
        return 255;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.TEXT};
    }

    @Override
    public @NotNull Integer parse(String[] args) {
        try {
            return Integer.valueOf(args[0]);
        } catch (NumberFormatException e) {
            throw new AttributeParseException("Opacity must be an integer between 0 and 255.");
        }
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("0", "64", "128", "192", "255");
        }
        return Collections.emptyList();
    }
}
